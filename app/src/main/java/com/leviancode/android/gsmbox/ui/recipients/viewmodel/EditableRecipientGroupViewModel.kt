package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class EditableRecipientGroupViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: RecipientGroup? = null
    var data = RecipientGroup()

    val selectColorEvent = SingleLiveEvent<Int>()
    val closeDialogEvent = SingleLiveEvent<RecipientGroup>()

    fun setGroup(value: RecipientGroup){
        data = value
        original = value.copy()
    }

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveGroup(data)
        }
        closeDialogEvent.value = data
    }

    fun onIconColorClick() {
        selectColorEvent.value = data.getRecipientGroupIconColor()
    }

    fun setIconColor(color: Int) {
        data.setRecipientGroupIconColor(color)
    }

    fun isGroupEdited() = original != data

}