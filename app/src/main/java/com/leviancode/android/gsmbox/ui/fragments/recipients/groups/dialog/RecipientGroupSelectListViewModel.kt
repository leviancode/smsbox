package com.leviancode.android.gsmbox.ui.fragments.recipients.groups.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.core.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.core.data.repository.RecipientsRepository

class RecipientGroupSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var items = listOf<RecipientGroup>()
    var selectGroupId = 0

    fun getGroups(): LiveData<List<RecipientGroup>> = repository.getGroups().map { list ->
        list.onEach { group ->
            group.selected = group.recipientGroupId == selectGroupId
        }.also {
            items = list
        }
    }

    fun selectGroup(groupId: Int) {
        selectGroupId = groupId
    }

    fun onItemClick(item: RecipientGroup) {
        if (items.isNotEmpty() && selectGroupId != 0) {
            items.find { it.recipientGroupId == selectGroupId }?.let {
                it.selected = false
            }
        }
        item.selected = true
        selectGroupId = item.recipientGroupId
    }
}