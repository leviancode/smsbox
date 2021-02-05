package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    fun loadRecipientById(id: String?): Boolean{
        if (id.isNullOrBlank()) return false
        repository.getRecipientById(id)?.let {
            recipient.data = it
            return true
        }
        return false
    }

    fun saveRecipient(){
        repository.addRecipient(recipient.data)
    }

    fun removeRecipient(){
        repository.removeRecipient(recipient.data.id)
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