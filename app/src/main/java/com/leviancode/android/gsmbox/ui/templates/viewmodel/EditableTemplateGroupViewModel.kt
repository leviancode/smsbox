package com.leviancode.android.gsmbox.ui.templates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class EditableTemplateGroupViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var editMode = false
    val group = TemplateGroupObservable()
    val chooseColorLiveEvent = SingleLiveEvent<TemplateGroupObservable>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()
    val closeDialogLiveEvent = SingleLiveEvent<Boolean>()

    init {
        fieldsChecker()
    }

    fun loadGroupById(id: String) = flow {
        repository.getGroupById(id)?.let {
            group.data = it
            emit(it.name)
        }
    }

    private fun saveGroup() {
        viewModelScope.launch {
            repository.addGroup(group.data)
        }
    }

    fun removeGroup() {
        viewModelScope.launch {
            repository.deleteGroup(group.data)
        }
    }

    private fun updateGroup() {
        viewModelScope.launch {
            repository.updateGroup(group.data)
        }
    }

    fun onSaveClick(){
        closeDialogLiveEvent.value = true
        if (editMode) saveGroup()
        else updateGroup()
    }

    fun onIconColorClick() {
        chooseColorLiveEvent.value = group
    }

    fun isFieldsNotEmpty(): Boolean {
        return group.isFieldsNotEmpty()
    }

    private fun fieldsChecker() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val notEmpty = isFieldsNotEmpty()
                if (fieldsNotEmptyLiveEvent.value != notEmpty) {
                    fieldsNotEmptyLiveEvent.postValue(notEmpty)
                }
                delay(500)
            }
        }
    }
}