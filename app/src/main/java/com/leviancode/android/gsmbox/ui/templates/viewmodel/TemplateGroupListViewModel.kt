package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateGroupListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<List<TemplateGroup>> = repository.groups
    val addGroupLiveEvent = SingleLiveEvent<TemplateGroup>()
    val selectedGroupLiveEvent = SingleLiveEvent<TemplateGroup>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, TemplateGroup>>()

    fun onAddGroupClick(){
        addGroupLiveEvent.value = TemplateGroup()
    }

    fun onItemClick(item: TemplateGroup){
        selectedGroupLiveEvent.value = item
    }

    fun onPopupMenuClick(view: View, item: TemplateGroup){
        popupMenuLiveEvent.value = view to item
    }

    fun deleteGroup(item: TemplateGroup) {
        viewModelScope.launch {
            repository.deleteGroup(item)
        }
    }
}