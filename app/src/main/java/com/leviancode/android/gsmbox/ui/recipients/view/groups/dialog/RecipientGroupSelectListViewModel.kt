package com.leviancode.android.gsmbox.ui.recipients.view.groups.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var firstLoad = true
    var selectedItems = mutableListOf<RecipientGroup>()
    var data = listOf<RecipientGroup>()
    var multiSelectMode = false

    fun loadNotEmptyGroupsAndSelectByGroupId(groupId: Long) =
        repository.groupsWithRecipients.map { list ->
            list.filter {
                if (it.group.recipientGroupId == groupId) onItemClick(it.group)
                it.getRecipients().isNotEmpty()
            }.map { it.group }.also { data = it }
        }

    fun loadAllGroupsAndSelectByRecipientId(recipientId: Long) =
        repository.groupsWithRecipients.map { list ->
            if (firstLoad) {
                list.forEach { groupWithRecipients ->
                    groupWithRecipients.getRecipients().find { it.recipientId == recipientId }?.let {
                        onItemClick(groupWithRecipients.group)
                    }
                }
            } else {
                list.forEach { groupWithRecipients ->
                    if (selectedItems.contains(groupWithRecipients.group)){
                        groupWithRecipients.group.selected = true
                    }
                }
            }
            list.map { it.group }.also { data = it }
        }

    fun onItemClick(item: RecipientGroup) {
        if (multiSelectMode) {
            item.selected = !item.selected
            if (item.selected) selectedItems.add(item)
            else selectedItems.remove(item)
        } else {
            if (selectedItems.isNotEmpty()) {
                val lastSelectedItem = selectedItems[0]
                if (lastSelectedItem.recipientGroupId == item.recipientGroupId) return
                lastSelectedItem.selected = false
            }
            item.selected = true
            selectedItems.add(0, item)
        }
    }

    fun getSingleSelectedGroupId() = if (selectedItems.isNotEmpty()) {
        selectedItems[0].recipientGroupId
    } else 0

    fun getNewGroup(): RecipientGroup = repository.getNewRecipientGroup()
}