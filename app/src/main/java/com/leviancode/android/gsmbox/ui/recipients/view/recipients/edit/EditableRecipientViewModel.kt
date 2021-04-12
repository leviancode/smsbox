package com.leviancode.android.gsmbox.ui.recipients.view.recipients.edit

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.*
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: Recipient? = null
    var data: RecipientWithGroups

    val savedEvent = SingleLiveEvent<Unit>()
    val selectContactEvent = SingleLiveEvent<Unit>()
    val addToGroupEvent = SingleLiveEvent<Long>()
    val removeGroupEvent = SingleLiveEvent<View>()
    val addGroupViewsEvent = SingleLiveEvent<List<RecipientGroup>>()

    init {
        data = repository.getNewRecipientWithGroups()
    }

    fun onSaveClick() {
        viewModelScope.launch {
            repository.saveRecipientWithGroups(data)
        }
        savedEvent.call()
    }

    fun loadRecipient(recipientId: Long) {
        viewModelScope.launch {
            repository.getRecipientWithGroups(recipientId)?.let {
                data = it
                addGroupViewsEvent.value = it.groups
            }
            data.recipient.recipientId = recipientId
        }
        original = data.recipient.copy()
    }

    fun setPhoneNumber(phoneNumber: String){
        data.recipient.setPhoneNumber(phoneNumber)
    }

    fun setContact(contact: Contact) {
        data.recipient.apply {
            setName(contact.name)
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
        data.groups = groups.toMutableList()
    }

    fun recipientNamesWithoutCurrent(id: Long) = repository.recipients.map { list ->
        list.filter { it.recipientId != id }.map { it.getName() }
    }
}