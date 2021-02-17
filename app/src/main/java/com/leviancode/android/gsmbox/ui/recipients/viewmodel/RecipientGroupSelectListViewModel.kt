package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.data.model.RecipientGroupObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var lastSelectedItem: RecipientGroupObservable? = null
    val groups: LiveData<List<RecipientGroupObservable>> =
        Transformations.map(repository.groups) { list ->
            list.map { RecipientGroupObservable(it) }
        }
    val selectedItem = MutableLiveData<RecipientGroup>()

    fun onItemClick(item: RecipientGroupObservable) {
        if (lastSelectedItem?.getGroupId() == item.getGroupId()) return

        lastSelectedItem?.let { it.selected = false }
        item.selected = true
        lastSelectedItem = item
        selectedItem.value = item.model
    }
}