package com.leviancode.android.gsmbox.ui.recipients.view.groups.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class EditableRecipientGroupViewModel : ViewModel() {
    private val repository = RecipientsRepository
    private var original: RecipientGroup? = null
    var data = RecipientGroup()

    val selectColorEvent = SingleLiveEvent<String>()
    val closeDialogEvent = SingleLiveEvent<Int>()

    fun getNamesWithoutCurrent(id: Int) = repository.groups.map { list ->
        list.filter { it.recipientGroupId != id }.map { it.getName() }
    }

    fun loadGroup(id: Int){
        viewModelScope.launch {
            repository.getGroupById(id)?.let { group ->
                data = group
            }
        }
        original = data.copy()
    }

    fun onSaveClick(){
        viewModelScope.launch {
            closeDialogEvent.value = repository.saveGroup(data)
        }
    }

    fun onIconColorClick() {
        selectColorEvent.value = data.getIconColor()
    }

    fun setIconColor(color: String) {
        data.setIconColor(color)
    }

    fun isGroupEdited() = original != data

}