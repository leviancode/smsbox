package com.leviancode.android.gsmbox.ui.screens.recipients.groups.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupsUI
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipientGroupMultiSelectListViewModel(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
) : ViewModel() {
    private var selectedGroups = mutableListOf<RecipientGroupUI>()

    val groups: LiveData<List<RecipientGroupUI>>
        get() = fetchRecipientGroupsUseCase.getAll().map { list ->
            list.toRecipientGroupsUI()
                .onEach { group ->
                    group.selected = selectedGroups.contains(group)
                }
        }.asLiveData()

    fun selectGroups(groupIds: IntArray) {
        viewModelScope.launch {
            val groups = fetchRecipientGroupsUseCase.getByIds(groupIds.toList())
                .map { it.toRecipientGroupUI() }
            selectedGroups.addAll(groups)
        }
    }

    fun onItemClick(item: RecipientGroupUI) {
        item.selected = !item.selected
        if (item.selected) {
            selectedGroups.add(item)
        } else {
            selectedGroups.remove(item)
        }
    }

    fun getSelectedGroups() = selectedGroups

    fun selectNewGroup(group: RecipientGroupUI) {
        selectedGroups.add(group)
    }
}