package com.leviancode.android.gsmbox.ui.screens.recipients.groups.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupsUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.map

class RecipientGroupSelectListViewModel(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
) : ViewModel() {
    private val _selectedGroup = SingleLiveEvent<Int>()
    val selectedGroup: LiveData<Int> = _selectedGroup

    fun getGroups(groupId: Int): LiveData<List<RecipientGroupUI>>
       = fetchRecipientGroupsUseCase.getAll().map { list ->
           list.filter { it.id != groupId }.toRecipientGroupsUI()
        }.asLiveData()

    fun onItemClick(item: RecipientGroupUI) {
        _selectedGroup.value = item.id
    }
}