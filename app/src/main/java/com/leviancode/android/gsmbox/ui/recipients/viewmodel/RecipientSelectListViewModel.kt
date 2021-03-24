package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.Contact
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class RecipientSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val recipients: LiveData<List<Recipient>> = repository.recipients
    var selectedItems = mutableListOf<Recipient>()
    var multiSelectMode = false

    fun loadRecipientsAndSelectByPhoneNumber(phoneNumber: String?) = repository.recipients.map { list ->
        list.onEach { if (it.getPhoneNumber() == phoneNumber) onItemClick(it) }
    }

    fun loadRecipientsAndSelectByGroupId(groupId: String?) = repository.recipientsWithGroups.map { list ->
        list.onEach { recipientsWithGroups ->
            recipientsWithGroups.groups.find { it.recipientGroupId == groupId }?.let {
                onItemClick(recipientsWithGroups.recipient)
            }
        }.map { it.recipient }
    }

    fun onItemClick(item: Recipient) {
        if (multiSelectMode){
            item.selected = !item.selected
            if (item.selected) selectedItems.add(item)
            else selectedItems.remove(item)
        } else {
            if (selectedItems.isNotEmpty()){
                val lastSelectedItem = selectedItems[0]
                if (lastSelectedItem.recipientId == item.recipientId) return
                lastSelectedItem.selected = false
            }
            item.selected = true
            selectedItems.add(0, item)
        }
    }

    fun selectRecipientByContactUri(context: Context, uri: Uri?): Recipient? {
        return ContactsManager.parseUri(context, uri)?.let { contact ->
            repository.contactToRecipient(contact).also { onItemClick(it) }
        }
    }

    fun getSingleSelectedRecipient() = selectedItems[0]
}