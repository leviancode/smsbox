package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class EditableTemplateViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val data = TemplateObservable()

    val addNumberFieldLiveEvent = MutableLiveData<RecipientObservable>()
    val removeRecipientLiveEvent = SingleLiveEvent<View>()
    val openRecipientDialogLiveEvent = SingleLiveEvent<Recipient>()

    val selectColorLiveEvent = SingleLiveEvent<Int>()
    val selectContactLiveEvent = SingleLiveEvent<Recipient>()
    val fieldsNotEmptyLiveEvent = SingleLiveEvent<Boolean>()
    val closeDialogLiveEvent = SingleLiveEvent<Boolean>()

    fun addRecipient(recipient: Recipient) {
        data.addRecipient(recipient)
        addNumberFieldLiveEvent.value = RecipientObservable().apply {
            model = recipient
        }
    }

    fun onAddNumberClick(){
        addRecipient(Recipient())
    }

    fun loadTemplateById(id: String) = flow {
        repository.getTemplateById(id)?.let { result ->
            data.model = result
            result.recipients.forEach { addRecipient(it) }
            emit(result.name)
        }
    }

    fun createTemplate(groupId: String?) {
        addRecipient(Recipient())
        groupId?.let {
            data.setGroupId(it)
        }
    }

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveTemplate(data.model)
        }
        closeDialogLiveEvent.value = true
    }
    
    fun onSaveRecipientClick(recipient: Recipient) {
        openRecipientDialogLiveEvent.value = recipient
    }

    fun onIconColorClick() {
        selectColorLiveEvent.value = data.getTemplateIconColor()
    }

    fun onContactsClick(recipient: Recipient) {
        selectContactLiveEvent.value = recipient
    }

    fun onRemoveRecipientClick(view: View, recipient: Recipient) {
        data.removeRecipient(recipient)
        removeRecipientLiveEvent.value = view
    }

    fun setIconColor(color: Int) {
        data.setTemplateIconColor(color)
    }

    fun isFieldsNotEmpty(): Boolean {
        return data.isFieldsFilled()
    }
}