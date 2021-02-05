package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val templates: LiveData<List<Template>> = repository.templates
    val createGroupLiveEvent = SingleLiveEvent<Unit>()

    fun addTemplate(template: Template) {
        viewModelScope.launch {
            repository.addTemplate(template)
        }
    }

    fun removeTemplate(template: Template) {
        repository.removeTemplate(template)
    }

    fun onCreateGroupClick(){
        createGroupLiveEvent.call()
    }
}