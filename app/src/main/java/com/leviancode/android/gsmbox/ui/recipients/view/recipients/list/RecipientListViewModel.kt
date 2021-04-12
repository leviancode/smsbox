package com.leviancode.android.gsmbox.ui.recipients.view.recipients.list

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var recipients: LiveData<List<Recipient>> = repository.recipients

    val addRecipientEvent = SingleLiveEvent<Long>()
    val recipientPopupMenuEvent = SingleLiveEvent<Pair<View, Recipient>>()

    fun onRecipientPopupMenuClick(view: View, item: Recipient) {
        recipientPopupMenuEvent.value = view to item
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
            repository.updateAllRecipients(list)
        }
    }

    fun addRecipientToGroups(recipient: Recipient, groups: List<RecipientGroup>) {
        viewModelScope.launch {
            repository.saveRecipientWithGroups(RecipientWithGroups(recipient, groups.toMutableList()))
        }
    }
}