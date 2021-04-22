package com.leviancode.android.gsmbox.ui.templates.view.groups.list

import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TemplateGroupListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val startDragEvent = SingleLiveEvent<RecyclerView.ViewHolder>()
    val addGroupEvent = SingleLiveEvent<Int>()
    val selectedGroupEvent = SingleLiveEvent<TemplateGroup>()
    val popupMenuEvent = SingleLiveEvent<Pair<View, GroupWithTemplates>>()

    fun getGroupsWithTemplates() = repository.getGroupsWithTemplates()

    fun onAddGroupClick(){
        addGroupEvent.value = 0
    }

    fun onItemClick(item: TemplateGroup){
        selectedGroupEvent.value = item
    }

    fun onPopupMenuClick(view: View, item:GroupWithTemplates){
        popupMenuEvent.value = view to item
    }

    fun deleteGroup(item: GroupWithTemplates) {
        viewModelScope.launch {
            repository.deleteGroupWithTemplates(item)
        }
    }

    fun updateGroupsOrder(list: List<GroupWithTemplates>) {
        viewModelScope.launch {
            repository.insertAllGroups(list.map { it.group })
        }
    }
}