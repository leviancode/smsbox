package com.leviancode.android.gsmbox.ui.templates.view.templates.edit

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditableTemplateViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: TemplateWithRecipients? = null
    var data: TemplateWithRecipients
    var recipientGroupMode = false

    private val _fieldsCorrect = MutableLiveData<Boolean>()
    val fieldsCorrect: LiveData<Boolean> = _fieldsCorrect

    val addRecipientViewEvent = SingleLiveEvent<Recipient>()
    val addAllRecipientsViewEvent = SingleLiveEvent<List<Recipient>>()
    val removeRecipientViewEvent = SingleLiveEvent<View>()
    val removeAllRecipientViewsEvent = SingleLiveEvent<Unit>()
    val saveRecipientEvent = SingleLiveEvent<Recipient>()

    val selectRecipientGroupEvent = SingleLiveEvent<Int>()
    val selectRecipientEvent = SingleLiveEvent<Recipient>()
    val selectColorEvent = SingleLiveEvent<String>()
    val savedEvent = SingleLiveEvent<Unit>()

    init {
        data = repository.getNewTemplateWithRecipients(0)
        fieldsChecker()
    }

    fun getRecipientNumbersLiveData() = RecipientsRepository.getRecipientNumbersLiveData()

    suspend fun getRecipientNumbers() = RecipientsRepository.getRecipientNumbers()

    fun loadTemplate(id: Int, groupId: Int): LiveData<TemplateWithRecipients> {
        val liveData = MutableLiveData<TemplateWithRecipients>()
        viewModelScope.launch {
            if (id != 0){
                repository.getTemplateWithRecipients(id)?.let {
                    if (it.recipients == null){
                        it.recipients = data.recipients
                    }
                    data = it
                    if(data.recipients!!.getRecipients().isEmpty()){
                        data.recipients!!.addRecipient(RecipientsRepository.getNewRecipient())
                    }
                    original = it.copy()
                    addAllRecipientsViewEvent.postValue(it.recipients!!.getRecipients())
                }
                liveData.value = data
            } else {
                data.template.templateGroupId = groupId
                original = data.copy()
                onAddRecipientClick()
                liveData.value = data
            }
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
        addRecipientViewEvent.value = recipient
    }

    fun onSelectRecipientGroupsClick() {
        selectRecipientGroupEvent.value = data.recipients?.group?.recipientGroupId
    }

    fun onSelectRecipientClick(recipient: Recipient) {
        selectRecipientEvent.value = recipient
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

    fun isTemplateEdited(): Boolean {
        return original != data
    }

    fun setRecipientGroup(groupId: Int) {
        removeAllRecipientViewsEvent.call()
        viewModelScope.launch  {
            RecipientsRepository.getGroupWithRecipients(groupId)?.let {
                data.recipients = it
                addAllRecipientsViewEvent.value = it.getRecipients()
            }
        }
    }

    private fun getListClone(list: List<Recipient>): List<Recipient> {
        val result = mutableListOf<Recipient>()
        list.forEach { result.add(it.copy()) }
        return result
    }

    fun updateRecipient(old: Recipient, new: Recipient) {
        old.apply {
            recipientId = new.recipientId
            setName(new.getName())
            setPhoneNumber(new.getPhoneNumber())
        }
    }

    fun getNamesExclusiveById(id: Int) = repository.getTemplateNamesExclusiveById(id)

    private fun fieldsChecker() {
        viewModelScope.launch {
            while (isActive) {
                _fieldsCorrect.value = data.isFieldsFilledAndCorrect()
                delay(500)
            }
        }
    }

    fun setTemplateNameUnique(unique: Boolean) {
        data.template.isNameUnique = unique
    }
}
