package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.list

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.*
import com.leviancode.android.gsmbox.ui.entities.recipients.*
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientListViewModel(
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val saveRecipientsUseCase: SaveRecipientsUseCase,
    private val deleteRecipientsUseCase: DeleteRecipientsUseCase
) : ViewModel() {
    val addRecipientEvent = SingleLiveEvent<Int>()
    val recipientPopupMenuEvent = SingleLiveEvent<Pair<View, RecipientWithGroupsUI>>()

    val recipients: LiveData<List<RecipientUI>>
        get() = fetchRecipientsUseCase.getRecipientsObservable().asLiveData()
            .map { it.toRecipientsUI() }

    fun onRecipientPopupMenuClick(view: View, item: RecipientUI) {
        viewModelScope.launch {
            fetchRecipientsUseCase.getRecipientWithGroupsById(item.id)?.toUIRecipientWithGroups()
                ?.let {
                    recipientPopupMenuEvent.value = view to it
                }
        }
    }

    fun deleteRecipient(item: RecipientUI) {
        viewModelScope.launch {
            deleteRecipientsUseCase.delete(item.toDomainRecipient())
        }
    }

    fun updateRecipient(recipient: RecipientUI) {
        viewModelScope.launch {
            saveRecipientsUseCase.save(recipient.toDomainRecipient())
        }
    }

    fun onAddRecipientClick() {
        addRecipientEvent.value = 0
    }

    fun updateRecipientsOrder(list: List<RecipientUI>) {
        viewModelScope.launch {
            saveRecipientsUseCase.save(list.toDomainRecipients())
        }
    }

    fun addRecipientToGroups(item: RecipientUI, groups: List<RecipientGroupUI>) {
        viewModelScope.launch {
            saveRecipientsUseCase.save(
                RecipientWithGroupsUI(
                    recipient = item,
                    groups = groups.toMutableList()
                ).toDomainRecipientWithGroups()
            )
        }
    }
}