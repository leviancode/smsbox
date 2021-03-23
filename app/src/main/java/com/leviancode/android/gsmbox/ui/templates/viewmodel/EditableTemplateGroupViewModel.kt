package com.leviancode.android.gsmbox.ui.templates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class EditableTemplateGroupViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: TemplateGroup? = null
    var data = TemplateGroup()

    val chooseColorLiveEvent = SingleLiveEvent<Int>()
    val savedLiveEvent = SingleLiveEvent<Unit>()

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

    fun setIconColor(color: Int) {
        data.setIconColor(color)
    }

    fun isGroupEdited() = original != data
}