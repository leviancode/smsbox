package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val templates: LiveData<List<Template>> = repository.templates

    fun addTemplate(template: Template) {
        repository.addTemplate(template)
    }

    fun removeTemplate(template: Template) {
        repository.removeTemplate(template)
    }
}