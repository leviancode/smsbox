package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent

class RecipientsViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val recipientList = mutableListOf<Recipient>()
    val addRecipientLiveEvent = SingleLiveEvent<Recipient>()
    val saveDialogLiveEvent = SingleLiveEvent<Recipient>()

    fun createRecipient(): Recipient{
        val recipient = Recipient()
        recipientList.add(recipient)
        return recipient
    }

    fun showSaveDialog(recipient: Recipient){
        saveDialogLiveEvent.value = recipient
    }

    fun saveRecipient(recipient: Recipient){
        repository.addRecipient(recipient)
    }


}