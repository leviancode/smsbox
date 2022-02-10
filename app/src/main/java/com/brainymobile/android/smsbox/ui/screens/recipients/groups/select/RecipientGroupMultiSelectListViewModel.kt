package com.brainymobile.android.smsbox.ui.screens.recipients.groups.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.ui.entities.recipients.toRecipientGroupsUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipientGroupMultiSelectListViewModel @Inject constructor(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
) : ViewModel() {
    private var groups = mutableListOf<RecipientGroupUI>()
    val selectedGroups: List<RecipientGroupUI> get() = groups.filter { it.selected }

    private val _groupsFlow = MutableSharedFlow<List<RecipientGroupUI>>()
    val groupsFlow = _groupsFlow.asSharedFlow()

    fun loadAndSelectGroupsByIds(groupIds: List<Int>) {
        viewModelScope.launch {
            fetchRecipientGroupsUseCase.getAll().toRecipientGroupsUI().onEach { group ->
                if (groups.find { it.id == group.id } == null) {
                    group.selected = groupIds.contains(group.id)
                    groups.add(group)
                }
            }.also {
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