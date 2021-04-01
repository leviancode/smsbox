package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var firstLoad = true
    var selectedItems = mutableListOf<RecipientGroup>()
    var data = listOf<RecipientGroup>()
    var multiSelectMode = false

    fun loadNotEmptyGroupsAndSelectByGroupId(groupId: String?) =
        repository.groupsWithRecipients.map { list ->
            list.filter {
                if (it.group.recipientGroupId == groupId) onItemClick(it.group)
                it.recipients.isNotEmpty()
            }.map { it.group }.also { data = it }
        }

    fun loadAllGroupsAndSelectByRecipientId(recipientId: String) =
        repository.groupsWithRecipients.map { list ->
            if (firstLoad) {
                list.forEach { groupWithRecipients ->
                    groupWithRecipients.recipients.find { it.recipientId == recipientId }?.let {
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

    fun getSingleSelectedGroupId() = selectedItems[0].recipientGroupId

    fun getNewGroup(): RecipientGroup = repository.getNewRecipientGroup()
}