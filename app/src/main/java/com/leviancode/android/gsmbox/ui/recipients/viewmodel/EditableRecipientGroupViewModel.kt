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

    val chooseColorLiveEvent = SingleLiveEvent<Int>()
    val closeDialogLiveEvent = SingleLiveEvent<RecipientGroup>()

    fun setGroup(value: RecipientGroup){
        data = value
        original = value.copy()
    }

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveGroup(data)
        }
        closeDialogLiveEvent.value = data
    }

    fun onIconColorClick() {
        chooseColorLiveEvent.value = data.getRecipientGroupIconColor()
    }

    fun setIconColor(color: Int) {
        data.setRecipientGroupIconColor(color)
    }

    fun isGroupEdited() = original != data

}