package com.leviancode.android.gsmbox.ui.recipients.recipients.list

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val addRecipientEvent = SingleLiveEvent<Int>()
    val recipientPopupMenuEvent = SingleLiveEvent<Pair<View, RecipientWithGroups>>()

    fun getRecipients(): LiveData<List<Recipient>> = repository.getRecipients()

    fun onRecipientPopupMenuClick(view: View, item: Recipient) {
        viewModelScope.launch {
            repository.getRecipientWithGroups(item.recipientId)?.let {
                recipientPopupMenuEvent.value = view to it
            }
        }
    }

    fun deleteRecipient(item: Recipient) {
        viewModelScope.launch {
            repository.deleteRecipient(item)
        }
    }

    fun updateRecipient(recipient: Recipient) {
        viewModelScope.launch {
            repository.saveRecipient(recipient)
        }
    }

    fun onAddRecipientClick() {
        addRecipientEvent.value = 0
    }

    fun updateRecipientsOrder(list: List<Recipient>) {
        viewModelScope.launch {
            repository.insertAllRecipients(list)
        }
    }

    fun addRecipientToGroups(item: Recipient, ids: List<Int>) {
        viewModelScope.launch {
            repository.saveRecipientWithGroups(item, ids)
        }
    }
}