package com.leviancode.android.gsmbox.ui.recipients.view.groups.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var selectedItems = mutableListOf<RecipientGroup>()

    fun loadNotEmptyGroupsAndSelectByGroupId(groupId: Int) =
        repository.groupsWithRecipients.map { list ->
            list.filter {
                if (it.group.recipientGroupId == groupId) {
                    onItemClick(it.group)
                }
                it.getRecipients().isNotEmpty()
            }.map { it.group }
        }

    fun onItemClick(item: RecipientGroup) {
        if (selectedItems.isNotEmpty()) {
            val lastSelectedItem = selectedItems[0]
            if (lastSelectedItem.recipientGroupId == item.recipientGroupId) return
            else lastSelectedItem.selected = false
        }
        item.selected = true
        selectedItems.add(0, item)
    }

    fun getSelectedGroupId() = if (selectedItems.isNotEmpty()) {
        selectedItems[0].recipientGroupId
    } else 0
}