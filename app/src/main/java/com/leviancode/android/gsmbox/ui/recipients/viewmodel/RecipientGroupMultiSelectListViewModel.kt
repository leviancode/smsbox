package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.log
import kotlinx.coroutines.launch

class RecipientGroupMultiSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var selectedItems = mutableListOf<RecipientGroup>()

    fun loadGroups(recipientId: String) = repository.groupsWithRecipients.map { list ->
        list.onEach { groupWithRecipients ->
            groupWithRecipients.recipients.find { it.recipientId == recipientId }?.let {
                onItemClick(groupWithRecipients.group)
            }
        }.map { it.group }
    }

    fun onItemClick(item: RecipientGroup) {
        item.selected = !item.selected
        if (item.selected){
            selectedItems.add(item)
        } else {
            selectedItems.remove(item)
        }
    }

}