package com.leviancode.android.gsmbox.ui.viewmodel

import android.view.View
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
import kotlinx.coroutines.launch

class TemplateViewModel : ViewModel() {
    val template = TemplateObservable()
    val recipients = mutableListOf<Recipient>()

    val addRecipientLiveEvent = SingleLiveEvent<RecipientObservable>()
    val removeRecipientLiveEvent = SingleLiveEvent<View>()
    val saveRecipientDialogLiveEvent = SingleLiveEvent<RecipientObservable>()
    val selectColorLiveEvent = SingleLiveEvent<Int>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()
    val selectContactLiveEvent = SingleLiveEvent<RecipientObservable>()

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
        TemplatesRepository.addTemplate(template.data)
    }

    fun saveRecipient(recipient: Recipient){
        RecipientsRepository.addOrUpdateRecipient(recipient)
    }

    fun onSaveRecipientClick(recipient: RecipientObservable){
        saveRecipient(recipient.data)
        saveRecipientDialogLiveEvent.value = recipient
    }

    fun onIconColorClick(){
        selectColorLiveEvent.value = template.getIconColor()
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

    private fun getNotEmptyRecipients(): MutableList<Recipient>{
        return recipients.filter { it.phoneNumber.isNotBlank() }.toMutableList()
    }

    private fun fieldsChecker(){
        viewModelScope.launch(Dispatchers.IO) {
            while(true){
                val notEmpty = isFieldsNotEmpty()
                if (fieldsNotEmptyLiveEvent.value != notEmpty) {
                    fieldsNotEmptyLiveEvent.postValue(notEmpty)
                }
                delay(1000)
            }
        }
    }
}