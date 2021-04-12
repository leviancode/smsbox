package com.leviancode.android.gsmbox.ui.templates.view.groups.list

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
    val startDragEvent = SingleLiveEvent<RecyclerView.ViewHolder>()
    val addGroupEvent = SingleLiveEvent<Long>()
    val selectedGroupEvent = SingleLiveEvent<TemplateGroup>()
    val popupMenuEvent = SingleLiveEvent<Pair<View, TemplateGroup>>()

    fun getGroups() = repository.getTemplateGroups()

    fun getGroupsWithTemplates() = repository.getGroupsWithTemplates()

    fun onAddGroupClick(){
        addGroupEvent.value = 0
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

    fun updateGroupsOrder(list: List<GroupWithTemplates>) {
        viewModelScope.launch {
            repository.updateAllGroups(list.map { it.group })
        }
    }
}