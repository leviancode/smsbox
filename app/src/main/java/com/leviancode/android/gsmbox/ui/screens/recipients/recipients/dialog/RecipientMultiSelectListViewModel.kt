package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.*

class RecipientMultiSelectListViewModel(
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
