package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import javax.xml.transform.Templates

class TemplatesViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val groups: LiveData<List<TemplateGroup>> = repository.groups
    val templates: LiveData<List<Template>> = repository.templates

    fun addGroup(group: TemplateGroup) {
        repository.addGroup(group)
    }

    fun removeGroup(group: TemplateGroup) {
        repository.removeGroup(group)
    }

    fun addTemplate(template: Template) {
        repository.addTemplate(template)
    }

    fun removeTemplate(template: Template) {
        repository.removeTemplate(template)
    }
}