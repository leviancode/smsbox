package com.brainymobile.android.smsbox.ui.screens.templates.groups.list

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.DeleteTemplateGroupUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.UpdateTemplateGroupsUseCase
import com.brainymobile.android.smsbox.ui.entities.templates.TemplateGroupUI
import com.brainymobile.android.smsbox.ui.entities.templates.toDomainTemplateGroup
import com.brainymobile.android.smsbox.ui.entities.templates.toDomainTemplateGroups
import com.brainymobile.android.smsbox.ui.entities.templates.toTemplateGroupsUI
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TemplateGroupListViewModel(
    private val fetchUseCase: FetchTemplateGroupsUseCase,
    private val updateUseCase: UpdateTemplateGroupsUseCase,
    private val deleteUseCase: DeleteTemplateGroupUseCase,
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

    fun updateAll(items: List<TemplateGroupUI>) {
        viewModelScope.launch {
            updateUseCase.update(items.toDomainTemplateGroups())
        }
    }
}