package com.leviancode.android.gsmbox.ui.recipients.view.groups.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.extensions.ifTrue
import com.leviancode.android.gsmbox.utils.log

class RecipientGroupMultiSelectListViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var selectedGroupIds = mutableListOf<Int>()

    fun getGroups(): LiveData<List<RecipientGroup>> = repository.groups.map { list ->
        list.onEach { group ->
            group.selected = selectedGroupIds.contains(group.recipientGroupId)
        }
    }

    fun selectGroups(groupIds: IntArray) {
        selectedGroupIds.addAll(groupIds.toList())
    }

    fun onItemClick(item: RecipientGroup) {
        item.selected = !item.selected
        if (item.selected) {
            selectedGroupIds.add(item.recipientGroupId)
        } else {
            selectedGroupIds.remove(item.recipientGroupId)
        }
    }

    fun getSelectedGroupIds() = selectedGroupIds

    fun selectNewGroup(id: Int) {
        selectedGroupIds.add(id)
    }
}