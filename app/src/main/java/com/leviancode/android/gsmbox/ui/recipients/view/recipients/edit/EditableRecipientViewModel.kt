package com.leviancode.android.gsmbox.ui.recipients.view.recipients.edit

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.*
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.log
import kotlinx.coroutines.*

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: Recipient? = null
    var data: RecipientWithGroups

    val savedEvent = SingleLiveEvent<Unit>()
    val selectContactEvent = SingleLiveEvent<Unit>()
    val addToGroupEvent = SingleLiveEvent<String>()
    val removeGroupEvent = SingleLiveEvent<View>()

    init {
        data = repository.getNewRecipientWithGroups()
    }

    fun onSaveClick() {
        log("to save: ${data.groups}")
        viewModelScope.launch {
            repository.saveRecipientWithGroups(data)
        }
        savedEvent.call()
    }

    fun loadRecipientWithGroupsById(recipientId: String) = liveData {
        repository.getRecipientWithGroups(recipientId)?.let {
            data = it
        }
        emit(data)
    }

    fun setRecipient(recipient: Recipient) {
        data.recipient = recipient
        original = recipient.copy()
    }

    fun setContact(contact: Contact) {
        data.recipient.apply {
            setRecipientName(contact.name)
            setPhoneNumber(contact.phoneNumber)
        }
    }

    fun onAddToGroupClick() {
        addToGroupEvent.value = data.recipient.recipientId
    }

    fun onContactsClick() {
        selectContactEvent.call()
    }

    fun onRemoveGroupClick(view: View, group: RecipientGroup) {
        data.removeGroup(group)
        removeGroupEvent.value = view
    }

    fun isRecipientEdited() = original != data.recipient

    fun setGroups(groups: List<RecipientGroup>) {
        /*data.groups.clear()
        data.groups.addAll(groups)*/
        data.groups = groups.toMutableList()
        log("add: ${data.groups}")
    }

    fun recipientNamesWithoutCurrent(id: String) = repository.recipients.map { list ->
        list.filter { it.recipientId != id }.map { it.getRecipientName() }
    }
}