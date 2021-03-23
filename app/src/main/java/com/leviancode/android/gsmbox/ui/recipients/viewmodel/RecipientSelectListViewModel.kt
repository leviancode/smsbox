package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.recipients.Contact
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class RecipientSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var lastSelectedItem: Recipient? = null
    val recipients: LiveData<List<Recipient>> = repository.recipients
    private val _selectedItem = MutableLiveData<Recipient>()
    val selectedItem: LiveData<Recipient> = _selectedItem

    fun onItemClick(item: Recipient) {
        if (lastSelectedItem?.recipientId == item.recipientId) return

        lastSelectedItem?.let { it.selected = false }
        item.selected = true
        lastSelectedItem = item
        _selectedItem.value = item
    }

    fun contactUriToRecipient(context: Context, uri: Uri): Recipient? {
        return ContactsManager.parseUri(context, uri)?.let { contact ->
            contactToRecipient(contact)
        }
    }

    private fun contactToRecipient(contact: Contact): Recipient{
        return Recipient(recipientName = contact.name, phoneNumber = contact.phoneNumber)
    }
}