package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        repository.addGroup(group.data)
    }

    fun removeGroup(group: TemplateGroup) {
        repository.removeGroup(group.id)
    }

    fun onIconColorClick(){
        chooseColorLiveEvent.value = group
    }

    fun isFieldsNotEmpty(): Boolean {
        return group.isFieldsNotEmpty()
    }

    private fun fieldsChecker(){
        viewModelScope.launch(Dispatchers.IO) {
            while(true){
                val notEmpty = isFieldsNotEmpty()
                if (fieldsNotEmptyLiveEvent.value != notEmpty) {
                    fieldsNotEmptyLiveEvent.postValue(notEmpty)
                }
                delay(500)
            }
        }
    }
}