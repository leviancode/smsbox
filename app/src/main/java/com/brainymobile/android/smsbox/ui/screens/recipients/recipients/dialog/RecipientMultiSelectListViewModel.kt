package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientUI
import com.brainymobile.android.smsbox.ui.entities.recipients.toRecipientsWithGroupsUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipientMultiSelectListViewModel @Inject constructor(
    private val fetchRecipientsUseCase: FetchRecipientsUseCase
) : ViewModel() {
    val selectedItems = mutableListOf<RecipientUI>()

    fun loadRecipientsAndSelectByGroupId(groupId: Int) = liveData {
        val recipients = fetchRecipientsUseCase.getRecipientsWithGroups()
            .toRecipientsWithGroupsUI()
            .onEach { recipientWithGroups ->
                recipientWithGroups.groups.find { it.id == groupId }?.let {
                    onItemClick(recipientWithGroups.recipient)
                }
            }.map { it.recipient }
        emit(recipients)
    }

    fun onItemClick(item: RecipientUI) {
        item.selected.set(!item.selected.get())
        if (item.selected.get()) selectedItems.add(item)
        else selectedItems.remove(item)
    }
}

