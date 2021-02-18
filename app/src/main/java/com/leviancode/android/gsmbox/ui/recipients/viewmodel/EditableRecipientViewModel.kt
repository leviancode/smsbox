package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val data = RecipientObservable()
    val closeDialogLiveEvent = SingleLiveEvent<Unit>()
    val selectContactLiveEvent = SingleLiveEvent<Unit>()
    val selectGroupLiveEvent = SingleLiveEvent<String>()

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveRecipient(data.model)
        }
        closeDialogLiveEvent.call()
    }

    fun setRecipient(item: Recipient?) {
        item?.let { data.model = it }
    }

    fun setGroupName(name: String) {
        data.setGroupName(name)
    }

    fun onContactsClick(){
        selectContactLiveEvent.call()
    }

    fun onGroupsClick(){
        selectGroupLiveEvent.value = data.getGroupName()
    }

    fun onRemoveGroupClick(){
        data.setGroupName(null)
    }
}