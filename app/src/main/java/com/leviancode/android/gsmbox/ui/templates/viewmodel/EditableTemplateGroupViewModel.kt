package com.leviancode.android.gsmbox.ui.templates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class EditableTemplateGroupViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: TemplateGroup? = null
    var data = repository.getNewTemplateGroup()
    val chooseColorLiveEvent = SingleLiveEvent<String>()
    val savedLiveEvent = SingleLiveEvent<Unit>()

    fun namesWithoutCurrent(id: String) = repository.groups.map { list ->
        list.filter { it.templateGroupId != id }.map { it.getName() }
    }

    fun setGroup(value: TemplateGroup){
        data = value
        original = value.copy()
    }

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveGroup(data)
        }
        savedLiveEvent.call()
    }

    fun onIconColorClick() {
        chooseColorLiveEvent.value = data.getIconColor()
    }

    fun setIconColor(color: String) {
        data.setIconColor(color)
    }

    fun isGroupEdited() = original != data
}