package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipientListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    var recipientsLiveData = repository.data
    val addRecipientLiveEvent = SingleLiveEvent<Unit>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, Recipient>>()

    fun onAddRecipientClick(){
        addRecipientLiveEvent.call()
    }

    fun onPopupMenuClick(view: View, item: RecipientObservable){
        popupMenuLiveEvent.value = Pair(view, item.data)
    }

    fun deleteRecipient(item: Recipient) {
        viewModelScope.launch {
            repository.deleteRecipient(item)
        }
    }
}