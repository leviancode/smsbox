package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val data = RecipientObservable()
    val closeDialogLiveEvent = SingleLiveEvent<Unit>()

    fun onSaveClick(){
        viewModelScope.launch {
            repository.addRecipient(data.model)
        }
        closeDialogLiveEvent.call()
    }

    fun setRecipient(item: Recipient) {
        data.model = item
    }
}