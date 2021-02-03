package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipientViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val recipient = RecipientObservable()
    val saveLiveEvent = SingleLiveEvent<String>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()

    init {
        fieldsChecker()
    }

    fun loadRecipientById(id: String){
        repository.getRecipientById(id)?.let {
            recipient.recipient = it
        }
    }

    fun saveRecipient(){
        repository.addRecipient(recipient.recipient)
    }

    fun removeRecipient(){
        repository.removeRecipient(recipient.recipient)
    }

    private fun fieldsChecker(){
        viewModelScope.launch(Dispatchers.IO) {
            while(true){
                if (fieldsNotEmptyLiveEvent.value != recipient.isFieldsNotEmpty()) {
                    fieldsNotEmptyLiveEvent.postValue(recipient.isFieldsNotEmpty())
                }
                delay(500)
            }
        }
    }
}