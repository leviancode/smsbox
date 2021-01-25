package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.repository.TemplatesRepository

class TemplatesViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<MutableList<TemplateGroup>> = repository.groupsLiveData


    fun deleteGroup(group: TemplateGroup) {
        repository.removeGroup(group)
    }
}