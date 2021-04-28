package com.leviancode.android.gsmbox.ui.recipients.recipients.edit

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.*
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: RecipientWithGroups? = null
    private var data: RecipientWithGroups = repository.getNewRecipientWithGroups()

    val savedEvent = SingleLiveEvent<Unit>()
    val selectContactEvent = SingleLiveEvent<Unit>()
    val addToGroupEvent = SingleLiveEvent<IntArray>()
    val removeGroupEvent = SingleLiveEvent<View>()
    val addGroupViewsEvent = SingleLiveEvent<List<RecipientGroup>>()

    fun onSaveClick() {
        viewModelScope.launch {
            repository.saveRecipientWithGroups(data)
        }
        savedEvent.call()
    }

    fun loadRecipient(recipientId: Int): LiveData<Recipient> {
        val liveData = MutableLiveData<Recipient>()
        if (recipientId != 0){
            viewModelScope.launch {
                repository.getRecipientWithGroups(recipientId)?.let {
                    data = it
                    original = data.copy()
                    liveData.value = data.recipient
                    addGroupViewsEvent.value = data.groups
                }
            }
        } else {
            liveData.value = data.recipient
            original = data.copy()
        }
        return liveData
    }

    fun setPhoneNumber(phoneNumber: String){
        data.recipient.setPhoneNumber(phoneNumber)
    }

    fun setRecipient(recipient: Recipient) {
        data.recipient.apply {
            setName(recipient.getName())
            setPhoneNumber(recipient.getPhoneNumber())
        }
    }

    fun onAddToGroupClick() {
        addToGroupEvent.value = data.groups.map { it.recipientGroupId }.toIntArray()
    }

    fun onContactsClick() {
        selectContactEvent.call()
    }

    fun onRemoveGroupClick(view: View, group: RecipientGroup) {
        data.removeGroup(group)
        removeGroupEvent.value = view
    }

    fun isRecipientEdited() = original != data

    fun setGroups(ids: List<Int>) {
        viewModelScope.launch {
            data.groups = repository.getGroupByIds(ids).toMutableList()
            addGroupViewsEvent.value = data.groups
        }
    }

    fun recipientNamesWithoutCurrent(id: Int) = repository.getRecipients().map { list ->
        list.filter { it.recipientId != id }.map { it.getName() }
    }
}