package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroupObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var lastSelectedItem: RecipientGroupObservable? = null
    val groups = Transformations.map(repository.groupedRecipients) { list ->
            list.map { RecipientGroupObservable(it.group) }
        }
    val selectedItem = MutableLiveData<String>()

    fun onItemClick(item: RecipientGroupObservable) {
        if (lastSelectedItem?.getGroupId() == item.getGroupId()) return

        lastSelectedItem?.let { it.selected = false }
        item.selected = true
        lastSelectedItem = item
        selectedItem.value = item.getName()
    }
}