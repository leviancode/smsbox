package com.leviancode.android.gsmbox.ui.viewmodel

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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NewTemplateDialogViewModel : ViewModel() {
    val template = TemplateObservable()
    val recipients = mutableListOf<Recipient>()

    val addRecipientLiveEvent = MutableLiveData<RecipientObservable>()
    val removeRecipientLiveEvent = SingleLiveEvent<View>()
    val saveRecipientDialogLiveEvent = SingleLiveEvent<RecipientObservable>()

    val selectColorLiveEvent = SingleLiveEvent<TemplateObservable>()
    val selectContactLiveEvent = SingleLiveEvent<RecipientObservable>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()

    init {
        fieldsChecker()
    }

    fun addRecipient() {
        val recipient = RecipientObservable()
        recipients.add(recipient.data)
        addRecipientLiveEvent.value = recipient
    }

    fun saveTemplate(groupId: String){
        template.setGroupId(groupId)
        template.setRecipients(getNotEmptyRecipients())

        viewModelScope.launch {
            TemplatesRepository.addTemplate(template.data)
        }
    }

    private fun saveRecipient(recipient: Recipient){
        viewModelScope.launch {
            RecipientsRepository.addRecipient(recipient)
        }
    }

    fun onSaveRecipientClick(recipient: RecipientObservable){
        saveRecipient(recipient.data)
        saveRecipientDialogLiveEvent.value = recipient
    }

    fun onIconColorClick(){
        selectColorLiveEvent.value = template
    }

    fun onContactsClick(recipient: RecipientObservable){
        selectContactLiveEvent.value = recipient
    }

    fun onRemoveRecipientClick(view: View, recipient: RecipientObservable){
        recipients.remove(recipient.data)
        removeRecipientLiveEvent.value = view
    }

    fun isFieldsNotEmpty(): Boolean {
        return template.isFieldsNotEmpty() && getNotEmptyRecipients().size > 0
    }

    private fun getNotEmptyRecipients(): MutableList<String>{
        return recipients.filter { it.phoneNumber.isNotBlank() }
            .map { it.phoneNumber }
            .toMutableList()
    }

    private fun fieldsChecker(){
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive){
                val notEmpty = isFieldsNotEmpty()
                if (fieldsNotEmptyLiveEvent.value != notEmpty) {
                    fieldsNotEmptyLiveEvent.postValue(notEmpty)
                }
                delay(1000)
            }
        }
    }
}