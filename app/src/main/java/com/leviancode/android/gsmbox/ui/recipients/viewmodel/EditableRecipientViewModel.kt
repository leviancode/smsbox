package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*

class EditableRecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: Recipient? = null
    val data = RecipientObservable()
    val groups: LiveData<List<RecipientGroup>> = repository.groups
    val savedLiveEvent = SingleLiveEvent<Unit>()
    val selectContactLiveEvent = SingleLiveEvent<Unit>()
    val selectGroupLiveEvent = SingleLiveEvent<String>()

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveRecipient(data.model)
        }
        savedLiveEvent.call()
    }

    fun setRecipient(item: Recipient) {
        original = item.copy()
        data.model = item
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

    fun isRecipientEdited() = original != data.model
}