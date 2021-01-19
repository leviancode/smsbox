package com.leviancode.android.gsmbox.ui.templates

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.model.TemplateGroup
import com.leviancode.android.gsmbox.repository.TemplatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<MutableList<TemplateGroup>> = repository.groupsLiveData

    fun addGroup(group: TemplateGroup){
        repository.addGroup(group)
    }

    fun deleteGroup(group: TemplateGroup) {
        repository.removeGroup(group)
    }
}