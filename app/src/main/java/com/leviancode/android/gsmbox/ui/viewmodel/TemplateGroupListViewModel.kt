package com.leviancode.android.gsmbox.ui.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent

class TemplateGroupListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<List<TemplateGroup>> = repository.groups
    val addGroupLiveEvent = SingleLiveEvent<Unit>()

    fun addGroup(group: TemplateGroup) {
        repository.addGroup(group)
    }

    fun removeGroup(group: TemplateGroup) {
        repository.removeGroup(group.id)
    }

    fun onAddGroupClick(view: View){
        addGroupLiveEvent.call()
    }
}