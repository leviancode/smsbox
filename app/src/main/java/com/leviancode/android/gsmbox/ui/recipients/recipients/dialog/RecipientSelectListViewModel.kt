package com.leviancode.android.gsmbox.ui.recipients.recipients.dialog

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.log
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class RecipientSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var items = listOf<Recipient>()
    var selectedPhoneNumber = ""

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
                selectedPhoneNumber = phoneNumber
                recipient.selected = true
            } else {
                recipient.selected = false
            }
        }
    }

    fun onItemClick(item: Recipient) {
        if (items.isNotEmpty() && selectedPhoneNumber.isNotBlank()) {
            items.find { it.getPhoneNumber() == selectedPhoneNumber }?.let {
                it.selected = false
            }
        }
        item.selected = true
        selectedPhoneNumber = item.getPhoneNumber()
    }

    fun selectRecipientByContactUri(context: Context, uri: Uri?): Recipient? {
        return ContactsManager.parseUri(context, uri)?.also { onItemClick(it) }
    }
}