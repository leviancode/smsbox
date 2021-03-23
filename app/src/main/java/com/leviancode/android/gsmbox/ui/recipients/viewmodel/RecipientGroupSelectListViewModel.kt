package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var lastSelectedItem: RecipientGroup? = null
    val notEmptyGroups: LiveData<List<GroupWithRecipients>> = repository.groupsWithRecipients.map { list ->
        list.filter { it.recipients.isNotEmpty() }
    }
    private val _selectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String> = _selectedItem

    fun onItemClick(item: RecipientGroup) {
        if (lastSelectedItem?.recipientGroupId == item.recipientGroupId) return

        lastSelectedItem?.let { it.selected = false }
        item.selected = true
        lastSelectedItem = item
        _selectedItem.value = item.recipientGroupId
    }

}