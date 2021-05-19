package com.leviancode.android.gsmbox.ui.fragments.recipients.recipients.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.core.data.repository.RecipientsRepository

class RecipientMultiSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var selectedItems = mutableListOf<Recipient>()
    fun getRecipients(): LiveData<List<Recipient>> = repository.getRecipients()

    fun loadRecipientsAndSelectByGroupId(groupId: Int) = repository.getRecipientsWithGroups().map { list ->
        list.onEach { recipientsWithGroups ->
            recipientsWithGroups.groups.find { it.recipientGroupId == groupId }?.let {
                onItemClick(recipientsWithGroups.recipient)
            }
        }.map { it.recipient }
    }

    fun onItemClick(item: Recipient) {
        item.selected = !item.selected
        if (item.selected) selectedItems.add(item)
        else selectedItems.remove(item)
    }
}