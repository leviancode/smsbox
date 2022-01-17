package com.brainymobile.android.smsbox.ui.screens.recipients.groups.list

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.DeleteRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.SaveRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.DeleteRecipientsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.*
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipientGroupListViewModel(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val saveRecipientGroupsUseCase: SaveRecipientGroupsUseCase,
    private val deleteRecipientGroupsUseCase: DeleteRecipientGroupsUseCase,
    private val deleteRecipientsUseCase: DeleteRecipientsUseCase
) : ViewModel() {
    val addGroupEvent = SingleLiveEvent<Int>()
    val createGroupDialogDismissed = SingleLiveEvent<Unit>()
    val recipientPopupMenuEvent = SingleLiveEvent<Pair<View, RecipientWithGroupUI>>()
    val groupPopupMenuEvent = SingleLiveEvent<Pair<View, RecipientGroupUI>>()
    var recentlyChangedGroup: RecipientGroupUI? = null

    val recipientGroups: LiveData<List<RecipientGroupUI>>
        get() = fetchRecipientGroupsUseCase.getAllObservable().map { it.toRecipientGroupsUI() }.asLiveData()

    fun onGroupPopupMenuClick(view: View, item: RecipientGroupUI) {
        groupPopupMenuEvent.value = view to item
    }

    fun onRecipientPopupMenuClick(view: View, item: RecipientWithGroupUI) {
        recipientPopupMenuEvent.value = view to item
    }

    fun deleteGroup(item: RecipientGroupUI) {
        viewModelScope.launch {
            deleteRecipientGroupsUseCase.delete(item.toDomainRecipientGroup())
        }
    }

    fun deleteRecipient(item: RecipientUI) {
        viewModelScope.launch {
            deleteRecipientsUseCase.delete(item.toDomainRecipient())
        }
    }

    fun onAddGroupClick() {
        addGroupEvent.value = 0
    }

    fun clearGroup(item: RecipientGroupUI) {
        viewModelScope.launch {
            recentlyChangedGroup = item
            deleteRecipientGroupsUseCase.clearGroup(item.id)
        }
    }

    fun restoreGroupWithRecipients() {
        viewModelScope.launch {
            recentlyChangedGroup?.let { saveRecipientGroupsUseCase.save(it.toDomainRecipientGroup()) }
        }
    }

    fun removeRecipientFromGroup(item: RecipientWithGroupUI) {
        viewModelScope.launch {
            deleteRecipientGroupsUseCase.unbind(
                item.group.id,
                item.recipient.id
            )
        }
    }

    fun updateGroupWithRecipients(item: RecipientGroupUI) {
        viewModelScope.launch {
            saveRecipientGroupsUseCase.save(item.toDomainRecipientGroup())
        }
    }
}