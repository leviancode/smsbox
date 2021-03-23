package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateGroupListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<List<TemplateGroup>> = repository.groups
    val groupsWithTemplates: LiveData<List<GroupWithTemplates>> = repository.groupsWithTemplates
    val startDragEvent = SingleLiveEvent<RecyclerView.ViewHolder>()
    val addGroupEvent = SingleLiveEvent<TemplateGroup>()
    val selectedGroupEvent = SingleLiveEvent<TemplateGroup>()
    val popupMenuEvent = SingleLiveEvent<Pair<View, TemplateGroup>>()

    fun onAddGroupClick(){
        addGroupEvent.value = TemplateGroup()
    }

    fun onItemClick(item: TemplateGroup){
        selectedGroupEvent.value = item
    }

    fun onPopupMenuClick(view: View, item: TemplateGroup){
        popupMenuEvent.value = view to item
    }

    fun deleteGroup(item: TemplateGroup) {
        viewModelScope.launch {
            repository.deleteGroup(item)
        }
    }

    fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        startDragEvent.value = viewHolder
    }

    fun updateAll(list: List<TemplateGroup>) {
        viewModelScope.launch {
            repository.updateAllGroups(list)
        }
    }
}