package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroupObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class EditableRecipientGroupViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: RecipientGroup? = null
    var data = RecipientGroupObservable()

    val chooseColorLiveEvent = SingleLiveEvent<Int>()
    val closeDialogLiveEvent = SingleLiveEvent<Boolean>()

    fun setGroup(value: RecipientGroup){
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
        chooseColorLiveEvent.value = data.getIconColor()
    }

    fun setIconColor(color: Int) {
        data.setIconColor(color)
    }

    fun isGroupEdited() = original != data.model

}