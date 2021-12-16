package com.leviancode.android.gsmbox.ui.screens.recipients.groups.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupsUI
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipientGroupMultiSelectListViewModel(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
) : ViewModel() {
    private var groups = mutableListOf<RecipientGroupUI>()
    val selectedGroups: List<RecipientGroupUI> get() = groups.filter { it.selected }

    private val _groupsFlow = MutableSharedFlow<List<RecipientGroupUI>>()
    val groupsFlow = _groupsFlow.asSharedFlow()


    fun loadAndSelectGroupsByIds(groupIds: List<Int>) {
        viewModelScope.launch {
            fetchRecipientGroupsUseCase.getAll().map { list ->
                list.toRecipientGroupsUI()
                    .onEach { group ->
                        if (groups.find { it.id == group.id } == null){
                            group.selected = groupIds.contains(group.id)
                            groups.add(group)
                        }
                    }
            }.collect {
                _groupsFlow.emit(it)
            }
        }
    }

    fun onItemClick(item: RecipientGroupUI) {
        item.selected = !item.selected
    }

    fun selectNewGroup(groupId: Int) {
        viewModelScope.launch {
            groups.find { it.id == groupId }?.let { group ->
                onItemClick(group)
            }
        }
    }
}