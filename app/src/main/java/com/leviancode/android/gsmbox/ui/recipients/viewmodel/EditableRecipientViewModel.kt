package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var recipient = Recipient()
    val recipientName = MutableLiveData<String>()
    val recipientPhoneNUmber = MutableLiveData<String>()
    val closeDialogLiveEvent = SingleLiveEvent<Unit>()
    private var editMode = false

    val isFieldsFilled = MediatorLiveData<Boolean>().apply {
        fun update() {
            recipient.name = recipientName.value ?: ""
            recipient.phoneNumber = recipientPhoneNUmber.value ?: ""
            value = recipient.name.isNotBlank() && recipient.phoneNumber.isNotBlank()
        }
        addSource(recipientName) { update() }
        addSource(recipientPhoneNUmber) { update() }
        update()
    }

    fun loadRecipientById(id: String) = flow {
        repository.getRecipientById(id)?.let {
            recipient = it
            recipientName.value = it.name
            recipientPhoneNUmber.value = it.phoneNumber
            editMode = true
            emit(it.name)
        }
    }

    fun onSaveClick(){
        if (editMode) updateRecipient()
        else saveRecipient()
        closeDialogLiveEvent.call()
    }

    private fun saveRecipient() {
        viewModelScope.launch {
            repository.addRecipient(recipient)
        }
    }

    private fun updateRecipient() {
        viewModelScope.launch {
            repository.updateRecipient(recipient)
        }
    }

    fun removeRecipient() {
        viewModelScope.launch {
            repository.deleteRecipient(recipient)
        }
    }

    fun setRecipient(item: Recipient?) {
        item?.let {
            recipient = it
            recipientName.value = it.name
            recipientPhoneNUmber.value = it.phoneNumber
            editMode = true
        }
    }
}