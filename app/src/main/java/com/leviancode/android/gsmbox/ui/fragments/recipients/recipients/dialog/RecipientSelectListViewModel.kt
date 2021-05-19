package com.leviancode.android.gsmbox.ui.fragments.recipients.recipients.dialog

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.core.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.core.utils.managers.ContactsManager

class RecipientSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var items = listOf<Recipient>()
    var selectedRecipient: Recipient? = null

    fun selectCurrentRecipientAndGetListWithoutAlreadySelected(
        phoneNumberForSelect: String,
        alreadySelectedPhoneNumbers: Array<String>
    ): LiveData<List<Recipient>> = repository.getRecipients().map { list ->
        list.filter { recipient ->
            !alreadySelectedPhoneNumbers.contains(recipient.getPhoneNumber())
        }.also {
            items = it
            selectPhoneNumber(phoneNumberForSelect)
        }
    }

    private fun selectPhoneNumber(phoneNumber: String) {
        items.forEach { recipient ->
            if (recipient.getPhoneNumber() == phoneNumber){
                selectedRecipient = recipient
                recipient.selected = true
            } else {
                recipient.selected = false
            }
        }
    }

    fun onItemClick(item: Recipient) {
        if (items.isNotEmpty() && selectedRecipient != null) {
            items.find { it.getPhoneNumber() == selectedRecipient?.getPhoneNumber() }?.let {
                it.selected = false
            }
        }
        item.selected = true
        selectedRecipient = item
    }

    fun selectRecipientByContactUri(context: Context, uri: Uri?): Recipient? {
        return ContactsManager.parseUri(context, uri)?.also { onItemClick(it) }
    }
}