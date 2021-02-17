package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.clans.fab.FloatingActionMenu
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.dependantLiveData
import kotlinx.coroutines.launch

class RecipientsViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var recipients: LiveData<List<Recipient>> = repository.recipients
    var groups: LiveData<List<RecipientGroup>> = repository.groups
    var groupsForExpandableList = dependantLiveData(recipients, groups, defaultValue = listOf()){
        groups.value?.onEach { group ->
            recipients.value?.filter { group.groupId == it.groupId }
                .orEmpty()
                .also { group.recipients.addAll(it) }
        }.orEmpty()
    }

    val addRecipientLiveEvent = SingleLiveEvent<Recipient>()
    val addGroupLiveEvent = SingleLiveEvent<RecipientGroup>()
    val recipientPopupMenuLiveEvent = SingleLiveEvent<Pair<View, Recipient>>()
    val groupPopupMenuLiveEvent = SingleLiveEvent<Pair<View, RecipientGroup>>()

    fun onRecipientPopupMenuClick(view: View, item: Recipient){
        recipientPopupMenuLiveEvent.value = view to item
    }

    fun onGroupPopupMenuClick(view: View, item: RecipientGroup){
        groupPopupMenuLiveEvent.value = view to item
    }

    fun deleteRecipient(item: Recipient) {
        viewModelScope.launch {
            repository.deleteRecipient(item)
        }
    }

    fun deleteGroup(item: RecipientGroup) {
        viewModelScope.launch {
            repository.deleteGroup(item)
        }
    }

    fun onAddRecipientClick(view: View){
        (view.parent as FloatingActionMenu).close(true)
        addRecipientLiveEvent.value = Recipient()
    }

    fun onAddGroupClick(view: View){
        (view.parent as FloatingActionMenu).close(true)
        addGroupLiveEvent.value = RecipientGroup()
    }
}