package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TemplateGroupViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val group = TemplateGroupObservable()
    val chooseColorLiveEvent = SingleLiveEvent<TemplateGroupObservable>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()

    init {
        fieldsChecker()
    }

    fun saveGroup() {
        viewModelScope.launch {
            repository.addGroup(group.data)
        }
    }

    fun removeGroup() {
        viewModelScope.launch {
            repository.removeGroup(group.data)
        }
    }

    fun updateGroup(){
        viewModelScope.launch {
            repository.updateGroup(group.data)
        }
    }

    fun onIconColorClick(){
        chooseColorLiveEvent.value = group
    }

    fun isFieldsNotEmpty(): Boolean {
        return group.isFieldsNotEmpty()
    }

    private fun fieldsChecker(){
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive){
                val notEmpty = isFieldsNotEmpty()
                if (fieldsNotEmptyLiveEvent.value != notEmpty) {
                    fieldsNotEmptyLiveEvent.postValue(notEmpty)
                }
                delay(500)
            }
        }
    }
}