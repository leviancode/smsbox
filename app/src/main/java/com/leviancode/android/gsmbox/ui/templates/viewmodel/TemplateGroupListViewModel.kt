package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateGroupListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<List<TemplateGroup>> = repository.groups
    val addGroupLiveEvent = SingleLiveEvent<Unit>()
    val selectGroupLiveEvent = SingleLiveEvent<TemplateGroup>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, TemplateGroup>>()

    fun onAddGroupClick(){
        addGroupLiveEvent.call()
    }

    fun onItemClick(item: TemplateGroupObservable){
        selectGroupLiveEvent.value = item.data
    }

    fun onPopupMenuClick(view: View, item: TemplateGroupObservable){
        popupMenuLiveEvent.value = Pair(view, item.data)
    }

    fun deleteGroup(item: TemplateGroup) {
        viewModelScope.launch {
            repository.deleteGroup(item)
        }
    }
}