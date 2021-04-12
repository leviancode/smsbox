package com.leviancode.android.gsmbox.ui.templates.view.groups.edit

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

    fun getNamesExclusiveCurrent(name: String) = repository.getTemplateGroupNames().map { list ->
        list.filter { it != name }
    }

    fun loadGroup(id: Long){
        if (id != 0L) {
            viewModelScope.launch {
                repository.getGroupById(id)?.let { group ->
                    data = group
                }
            }
        }
        original = data.copy()
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