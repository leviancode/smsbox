package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientsUI
import com.leviancode.android.gsmbox.utils.managers.ContactsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipientSelectListViewModel(
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val contactsManager: ContactsManager
) : ViewModel() {
    private var items = listOf<RecipientUI>()
    var selectedRecipient: RecipientUI? = null

    fun selectCurrentRecipientAndGetListWithoutAlreadySelected(
        phoneNumberForSelect: String,
        alreadySelectedPhoneNumbers: List<String>
    ): Flow<List<RecipientUI>> =
        fetchRecipientsUseCase.getRecipientsObservable().map { list ->
            list.toRecipientsUI()
                .filter { recipient ->
                    !alreadySelectedPhoneNumbers.contains(recipient.getPhoneNumber())
                }.also {
                    items = it
                    selectPhoneNumber(phoneNumberForSelect)
                }
        }

    private fun selectPhoneNumber(phoneNumber: String) {
        items.forEach { recipient ->
            if (recipient.getPhoneNumber() == phoneNumber) {
                selectedRecipient = recipient
                recipient.selected.set(true)
            } else {
                recipient.selected.set(false)
            }
        }
    }

    fun onItemClick(item: RecipientUI) {
        if (items.isNotEmpty() && selectedRecipient != null) {
            items.find { it.getPhoneNumber() == selectedRecipient?.getPhoneNumber() }
                ?.selected?.set(false)
        }
        item.selected.set(true)
        selectedRecipient = item
    }

    fun selectRecipientByContactUri(uri: Uri?): RecipientUI? {
        return contactsManager.parseUri(uri)?.also { onItemClick(it) }
    }
}