package com.leviancode.android.gsmbox.ui.templates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditableTemplateGroupViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: TemplateGroup? = null
    var data = TemplateGroupObservable()

    val chooseColorLiveEvent = SingleLiveEvent<Int>()
    val closeDialogLiveEvent = SingleLiveEvent<Boolean>()

    fun setGroup(value: TemplateGroup){
        data.model = value
        original = value.copy()
    }

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveGroup(data.model)
        }
        closeDialogLiveEvent.value = true
    }

    fun onIconColorClick() {
        chooseColorLiveEvent.value = data.getTemplateGroupIconColor()
    }

    fun setIconColor(color: Int) {
        data.setTemplateGroupIconColor(color)
    }

    fun isGroupEdited() = original != data.model
}