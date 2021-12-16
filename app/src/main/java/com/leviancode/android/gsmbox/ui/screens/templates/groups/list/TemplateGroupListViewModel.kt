package com.leviancode.android.gsmbox.ui.screens.templates.groups.list

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.DeleteTemplateGroupUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateGroupUI
import com.leviancode.android.gsmbox.ui.entities.templates.toDomainTemplateGroup
import com.leviancode.android.gsmbox.ui.entities.templates.toTemplateGroupsUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TemplateGroupListViewModel(
    private val fetchUseCase: FetchTemplateGroupsUseCase,
    private val deleteUseCase: DeleteTemplateGroupUseCase
) : ViewModel() {
    val addGroupEvent = SingleLiveEvent<Int>()
    val selectedGroupEvent = SingleLiveEvent<TemplateGroupUI>()
    val popupMenuEvent = SingleLiveEvent<Pair<View, TemplateGroupUI>>()

    val data: Flow<List<TemplateGroupUI>>
        get() = fetchUseCase.getGroupsObservable().map { it.toTemplateGroupsUI() }

    fun onAddGroupClick() {
        addGroupEvent.value = 0
    }

    fun onItemClick(item: TemplateGroupUI) {
        selectedGroupEvent.value = item
    }

    fun onPopupMenuClick(view: View, item: TemplateGroupUI) {
        popupMenuEvent.value = view to item
    }

    fun deleteGroup(item: TemplateGroupUI) {
        viewModelScope.launch {
            deleteUseCase.delete(item.toDomainTemplateGroup())
        }
    }
}