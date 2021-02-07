package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class EditableTemplateViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var editMode = false
    val template = TemplateObservable()
    val recipients = mutableListOf<Recipient>()

    val addNumberFieldLiveEvent = MutableLiveData<RecipientObservable>()
    val removeRecipientLiveEvent = SingleLiveEvent<View>()
    val openRecipientDialogLiveEvent = SingleLiveEvent<Recipient>()

    val selectColorLiveEvent = SingleLiveEvent<TemplateObservable>()
    val selectContactLiveEvent = SingleLiveEvent<RecipientObservable>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()
    val closeDialogLiveEvent = SingleLiveEvent<Boolean>()

    init {
        fieldsChecker()
    }

    private fun setRecipients(numbers: List<String>) {
        for (number in numbers) {
            val recipient = RecipientObservable()
            recipient.setPhoneNumber(number)

            recipients.add(recipient.data)
            addNumberFieldLiveEvent.value = recipient
        }
    }

    fun onAddRecipient() {
        val recipient = RecipientObservable()
        recipients.add(recipient.data)
        addNumberFieldLiveEvent.value = recipient
    }

    fun loadTemplateById(id: String) = flow {
        repository.getTemplateById(id)?.let { result ->
            template.data = result
            setRecipients(result.recipients)
            editMode = true
            emit(result.name)
        }
    }

    fun createTemplate(groupId: String?) {
        onAddRecipient()
        groupId?.let {
            template.setGroupId(it)
        }
    }

    private fun saveTemplate() {
        template.setRecipients(getNotEmptyRecipients())
        viewModelScope.launch {
            repository.addTemplate(template.data)
        }
    }

    private fun updateTemplate() {
        template.setRecipients(getNotEmptyRecipients())
        viewModelScope.launch {
            repository.updateTemplate(template.data)
        }
    }

    fun onSaveClick(){
        closeDialogLiveEvent.value = true
        if (editMode) saveTemplate()
        else updateTemplate()
    }
    
    fun onSaveRecipientClick(recipient: RecipientObservable) {
        openRecipientDialogLiveEvent.value = recipient.data
    }

    fun onIconColorClick() {
        selectColorLiveEvent.value = template
    }

    fun onContactsClick(recipient: RecipientObservable) {
        selectContactLiveEvent.value = recipient
    }

    fun onRemoveRecipientClick(view: View, recipient: RecipientObservable) {
        recipients.remove(recipient.data)
        removeRecipientLiveEvent.value = view
    }

    fun isFieldsNotEmpty(): Boolean {
        return template.isFieldsNotEmpty() && getNotEmptyRecipients().size > 0
    }

    private fun getNotEmptyRecipients(): MutableList<String> {
        return recipients.filter { it.phoneNumber.isNotBlank() }
            .map { it.phoneNumber }
            .toMutableList()
    }

    private fun fieldsChecker() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val notEmpty = isFieldsNotEmpty()
                if (fieldsNotEmptyLiveEvent.value != notEmpty) {
                    fieldsNotEmptyLiveEvent.postValue(notEmpty)
                }
                delay(1000)
            }
        }
    }
}