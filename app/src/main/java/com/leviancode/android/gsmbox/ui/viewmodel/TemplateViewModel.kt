package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent

class TemplateViewModel : ViewModel() {
    val template = TemplateObservable()
    val recipients = mutableListOf<Recipient>()

    val addRecipientLiveEvent = SingleLiveEvent<RecipientObservable>()
    val saveRecipientDialogLiveEvent = SingleLiveEvent<String>()
    val chooseColorLiveEvent = SingleLiveEvent<Int>()

    fun addRecipient() {
        val recipient = RecipientObservable()
        recipients.add(recipient.recipient)
        addRecipientLiveEvent.value = recipient
    }

    fun saveTemplate(groupId: String){
        template.setGroupId(groupId)
        template.setRecipients(getNotEmptyRecipients())
        TemplatesRepository.addTemplate(template.template)
    }

    fun saveRecipient(recipient: Recipient){
        RecipientsRepository.addRecipient(recipient)
    }

    private fun getNotEmptyRecipients(): MutableList<Recipient>{
        return recipients.filter { it.phoneNumber.isNotBlank() }.toMutableList()
    }

    fun onSaveRecipientClick(recipient: RecipientObservable){
        saveRecipient(recipient.recipient)
        saveRecipientDialogLiveEvent.value = recipient.recipient.id
    }

    fun onIconColorClick(){
        chooseColorLiveEvent.value = template.getIconColor()
    }

    fun onContactsClick(recipient: RecipientObservable){

    }
}