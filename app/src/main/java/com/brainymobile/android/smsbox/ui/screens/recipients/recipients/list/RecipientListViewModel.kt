package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.list

import android.view.View
import androidx.lifecycle.*
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.DeleteRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.UpdateRecipientsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.*
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientListViewModel(
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val updateRecipientsUseCase: UpdateRecipientsUseCase,
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

    fun onAddRecipientClick() {
        addRecipientEvent.value = 0
    }

    fun updateRecipients(list: List<RecipientUI>) {
        viewModelScope.launch {
            updateRecipientsUseCase.update(list.toDomainRecipients())
        }
    }

    fun addRecipientToGroups(item: RecipientUI, groups: List<RecipientGroupUI>) {
        viewModelScope.launch {
            updateRecipientsUseCase.update(
                RecipientWithGroupsUI(
                    recipient = item,
                    groups = groups.toMutableList()
                ).toDomainRecipientWithGroups()
            )
        }
    }
}