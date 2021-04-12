package com.leviancode.android.gsmbox.ui.recipients.view.groups.list

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientGroupListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var groupsWithRecipients: LiveData<List<GroupWithRecipients>> = repository.groupsWithRecipients

    val addGroupEvent = SingleLiveEvent<Long>()
    val recipientPopupMenuEvent = SingleLiveEvent<Pair<View, RecipientWithGroup>>()
    val groupPopupMenuEvent = SingleLiveEvent<Pair<View, GroupWithRecipients>>()

    var recentlyChangedGroup: GroupWithRecipients? = null

    fun onGroupPopupMenuClick(view: View, item: GroupWithRecipients) {
        groupPopupMenuEvent.value = view to item
    }

    fun onRecipientPopupMenuClick(view: View, item: RecipientWithGroup) {
        recipientPopupMenuEvent.value = view to item
    }

    fun deleteGroup(item: RecipientGroup) {
        viewModelScope.launch {
            repository.deleteGroup(item)
        }
    }

    fun deleteRecipient(item: Recipient) {
        viewModelScope.launch {
            repository.deleteRecipient(item)
        }
    }

    fun onAddGroupClick() {
        addGroupEvent.value = 0
    }

    fun clearGroup(item: GroupWithRecipients) {
        viewModelScope.launch {
            recentlyChangedGroup = item
            repository.clearGroup(item.group)
        }
    }

    fun restoreGroupWithRecipients() {
        viewModelScope.launch {
            recentlyChangedGroup?.let { repository.saveGroupWithRecipients(it) }
        }
    }

    fun removeRecipientFromGroup(item: RecipientWithGroup) {
        viewModelScope.launch {
            repository.deleteGroupAndRecipientRelation(
                item.group.recipientGroupId,
                item.recipient.recipientId
            )
        }
    }

    fun updateGroupWithRecipients(item: GroupWithRecipients) {
        viewModelScope.launch {
            repository.saveGroupWithRecipients(item)
        }
    }
}