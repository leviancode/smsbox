package com.leviancode.android.gsmbox.ui.recipients.view

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.*

class NewRecipientDialogViewModel : ViewModel() {
    private val repository = RecipientsRepository
    val recipient = RecipientObservable()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()

    val nameLiveData = MutableLiveData<String>()
    val phoneNumberLiveData = MutableLiveData<String>()

    val isFieldsFilled = MediatorLiveData<Boolean>().apply {
        fun update(){
            val name = nameLiveData.value ?: return
            val number = phoneNumberLiveData.value ?: return

            value = name.isNotBlank() && number.isNotBlank()
        }
        addSource(nameLiveData){ update() }
        addSource(phoneNumberLiveData){ update() }
        update()
    }

    init {
        fieldsChecker()
    }

    fun loadRecipientById(id: String?){
        if (id.isNullOrBlank()) return
        viewModelScope.launch {
            repository.getRecipientById(id)?.let {
                recipient.data = it
            }
        }
    }

    fun saveRecipient(){
        viewModelScope.launch {
            repository.addRecipient(recipient.data)
        }
    }

    fun updateRecipient() {
        viewModelScope.launch {
            repository.updateRecipient(recipient.data)
        }
    }

    fun removeRecipient(){
        viewModelScope.launch {
            repository.removeRecipient(recipient.data)
        }
    }

    private fun fieldsChecker(){
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive){
                if (fieldsNotEmptyLiveEvent.value != recipient.isFieldsNotEmpty()) {
                    fieldsNotEmptyLiveEvent.postValue(recipient.isFieldsNotEmpty())
                }
                delay(500)
            }
        }
    }


}