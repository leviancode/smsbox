package com.leviancode.android.gsmbox.ui.fragments.templates.groups.edit

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.core.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.core.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class EditableTemplateGroupViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: TemplateGroup? = null
    private var data = repository.getNewTemplateGroup()
    val chooseColorLiveEvent = SingleLiveEvent<String>()
    val savedLiveEvent = SingleLiveEvent<Unit>()

    fun loadGroup(id: Int): LiveData<TemplateGroup> {
        val liveData = MutableLiveData<TemplateGroup>()
        viewModelScope.launch {
            if (id != 0) {
                repository.getGroupById(id)?.let { group ->
                    data = group
                }
            }
            original = data.copy()
            liveData.value = data
        }
        return liveData
    }

    fun onSaveClick() {
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