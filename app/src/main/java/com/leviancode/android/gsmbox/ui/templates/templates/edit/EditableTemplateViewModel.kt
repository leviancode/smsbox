package com.leviancode.android.gsmbox.ui.templates.templates.edit

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class EditableTemplateViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: TemplateWithRecipients? = null
    private var data: TemplateWithRecipients
    var recipientGroupMode = false

    private val _fieldsCorrect = MutableLiveData<Boolean>()
    val fieldsCorrect: LiveData<Boolean> = _fieldsCorrect

    val addSingleRecipientViewEvent = SingleLiveEvent<Recipient>()
    val addManyRecipientsViewEvent = SingleLiveEvent<List<Recipient>>()
    val removeRecipientViewEvent = SingleLiveEvent<View>()
    val removeAllRecipientViewsEvent = SingleLiveEvent<Unit>()
    val saveRecipientEvent = SingleLiveEvent<Recipient>()

    val selectRecipientGroupEvent = SingleLiveEvent<Int>()
    val selectRecipientEvent = SingleLiveEvent<Pair<Recipient, List<String>>>()
    val selectColorEvent = SingleLiveEvent<String>()
    val savedEvent = SingleLiveEvent<Unit>()

    init {
        data = repository.getNewTemplateWithRecipients(0)
    }

    val recipientNumbers = RecipientsRepository.getRecipientNumbersLiveData()

    fun loadTemplate(id: Int, groupId: Int): LiveData<TemplateWithRecipients> {
        val liveData = MutableLiveData<TemplateWithRecipients>()
        viewModelScope.launch {
            if (id != 0){
                repository.getTemplateWithRecipients(id)?.let {
                    if (it.recipients == null){
                        it.recipients = data.recipients
                    }
                    if(it.recipients!!.getRecipients().isEmpty()){
                        it.recipients!!.addRecipient(RecipientsRepository.getNewRecipient())
                    }
                    data = it
                    addManyRecipientsViewEvent.postValue(it.recipients!!.getRecipients())
                }
            } else {
                data.template.templateGroupId = groupId
                onAddRecipientClick()
            }
            liveData.value = data
        }.invokeOnCompletion {
            original = data.copy()
            fieldsChecker(data)
        }
        return liveData
    }

    fun onAddRecipientClick() {
        RecipientsRepository.getNewRecipient().also {
            data.addRecipient(it)
            addRecipientView(it)
        }
    }

    private fun addRecipientView(recipient: Recipient) {
        addSingleRecipientViewEvent.value = recipient
    }

    fun onSelectRecipientGroupsClick() {
        selectRecipientGroupEvent.value = data.recipients?.group?.recipientGroupId
    }

    fun onSelectRecipientClick(recipient: Recipient) {
        selectRecipientEvent.value =
            recipient to (data.recipients?.getRecipients()
                ?.filter { it.getPhoneNumber() != recipient.getPhoneNumber() }
                ?.map { it.getPhoneNumber() } ?: listOf())
    }

    fun onSaveClick() {
        viewModelScope.launch{
            repository.saveTemplateWithRecipients(data)
        }
        savedEvent.call()
    }

    fun onSaveRecipientClick(recipient: Recipient) {
        saveRecipientEvent.value = recipient
    }

    fun onIconColorClick() {
        selectColorEvent.value = data.template.getIconColor()
    }

    fun onRemoveRecipientClick(view: View, recipient: Recipient) {
        data.removeRecipient(recipient)
        removeRecipientViewEvent.value = view
    }

    fun setIconColor(color: String) {
        data.template.setIconColor(color)
    }

    fun isTemplateEdited(): Boolean = original != data

    suspend fun updateRecipientGroup(groupId: Int): GroupWithRecipients? {
        return RecipientsRepository.getGroupWithRecipients(groupId)?.also {
            data.recipients = it
            addManyRecipientsViewEvent.value = it.getRecipients()
        }
    }

    fun updateRecipientPhoneNumber(recipient: Recipient, phoneNumber: String) {
        recipient.setPhoneNumber(phoneNumber)
    }

    private fun fieldsChecker(data: TemplateWithRecipients) {
        viewModelScope.launch {
            while (isActive) {
                _fieldsCorrect.value = data.isFieldsFilledAndCorrect()
                delay(500)
            }
        }
    }
}
