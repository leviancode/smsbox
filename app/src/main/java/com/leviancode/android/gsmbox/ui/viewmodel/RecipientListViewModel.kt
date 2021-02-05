package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent

class RecipientListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var recipientsLiveData = repository.recipients
    val addRecipientLiveEvent = SingleLiveEvent<Unit>()


    fun onAddRecipientClick(){
        addRecipientLiveEvent.call()
    }
}