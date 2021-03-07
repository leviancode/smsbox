package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var lastSelectedItem: RecipientObservable? = null
    val recipients: LiveData<List<RecipientObservable>> =
        Transformations.map(repository.recipients) { list ->
            list.map { RecipientObservable(it) }
        }
    private val _selectedItem = MutableLiveData<Recipient>()
    val selectedItem: LiveData<Recipient> = _selectedItem

    fun onItemClick(item: RecipientObservable) {
        if (lastSelectedItem?.getRecipientId() == item.getRecipientId()) return

        lastSelectedItem?.let { it.selected = false }
        item.selected = true
        lastSelectedItem = item
        _selectedItem.value = item.model
    }
}