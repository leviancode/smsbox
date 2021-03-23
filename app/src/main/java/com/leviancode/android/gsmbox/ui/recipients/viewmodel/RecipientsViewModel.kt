package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientsViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var recipients: LiveData<List<Recipient>> = repository.recipients
    var groups: LiveData<List<RecipientGroup>> = repository.groups
    var groupWithRecipients: LiveData<List<GroupWithRecipients>> = repository.groupsWithRecipients

    val addRecipientEvent = SingleLiveEvent<Recipient>()
    val addGroupEvent = SingleLiveEvent<RecipientGroup>()
    val recipientPopupMenuEvent = SingleLiveEvent<Pair<View, Recipient>>()
    val recipientInGroupPopupMenuEvent = SingleLiveEvent<Pair<View, Recipient>>()
    val groupPopupMenuEvent = SingleLiveEvent<Pair<View, RecipientGroup>>()

    var recentlyChangedGroup: GroupWithRecipients? = null

    fun onRecipientPopupMenuClick(view: View, item: Recipient) {
        if (view.tag != null) {
            recipientInGroupPopupMenuEvent.value = view to item
        } else {
            recipientPopupMenuEvent.value = view to item
        }

    }

    fun onGroupPopupMenuClick(view: View, item: RecipientGroup) {
        groupPopupMenuEvent.value = view to item
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

    fun deleteGroup(item: RecipientGroup) {
        viewModelScope.launch {
            repository.deleteGroup(item)
        }
    }

    fun onAddRecipientClick() {
        addRecipientEvent.value = repository.getNewRecipient()
    }

    fun onAddGroupClick() {
        addGroupEvent.value = repository.getNewRecipientGroup()
    }

    fun clearGroup(group: RecipientGroup) {
        viewModelScope.launch {
            recentlyChangedGroup = repository.getGroupWithRecipients(group.recipientGroupId)
            repository.clearGroup(group)
        }
    }

    fun restoreGroupWithRecipients() {
        viewModelScope.launch {
            recentlyChangedGroup?.let { repository.saveGroupWithRecipients(it) }
        }
    }

    fun updateRecipientsOrder(list: List<Recipient>) {
        viewModelScope.launch {
            repository.updateAllRecipients(list)
        }
    }

    fun addRecipientToGroup(recipient: Recipient, group: RecipientGroup) {
        viewModelScope.launch {
            repository.saveGroupAndRecipientCrossRef(group.recipientGroupId, recipient.recipientId)
        }
    }

    fun removeRecipientFromGroup(recipient: Recipient) {
        recipientInGroupPopupMenuEvent.value?.let {
            try {
                val tag = it.first.tag as String
                viewModelScope.launch {
                    repository.deleteGroupAndRecipientCrossRef(tag, recipient.recipientId)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun addRecipientToGroups(recipient: Recipient, groups: List<RecipientGroup>) {
        viewModelScope.launch {
            repository.saveRecipientWithGroups(RecipientWithGroups(recipient, groups.toMutableList()))
        }
    }
}