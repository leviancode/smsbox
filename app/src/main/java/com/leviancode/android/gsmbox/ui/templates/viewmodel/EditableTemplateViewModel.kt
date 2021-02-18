package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class EditableTemplateViewModel : ViewModel() {
    private val repository = TemplatesRepository
    var original: Template? = null
    val data = TemplateObservable()
    val recipients = RecipientsRepository.recipients

    val addNumberFieldLiveEvent = SingleLiveEvent<RecipientObservable>()
    val removeRecipientLiveEvent = SingleLiveEvent<View>()
    val openRecipientDialogLiveEvent = SingleLiveEvent<Recipient>()

    val selectColorLiveEvent = SingleLiveEvent<Int>()
    val selectContactLiveEvent = MutableLiveData<RecipientObservable>()
    val savedLiveEvent = SingleLiveEvent<Unit>()

    init {
        fieldsChecker()
    }

    fun setTemplate(template: Template){
        data.model = template
        original = template.copy()
        if (template.recipients.isEmpty()) {
            onAddRecipientClick()
        }
    }

    private fun addNumberField(recipient: Recipient) {
        addNumberFieldLiveEvent.value = RecipientObservable(recipient)
    }

    fun onAddRecipientClick(){
        Recipient().let {
            data.addRecipient(it)
            addNumberField(it)
        }
    }

    fun onRecipientGroupClick(){

    }

    fun onSaveClick(){
        viewModelScope.launch {
            repository.saveTemplate(data.model)
        }
        savedLiveEvent.call()
    }
    
    fun onSaveRecipientClick(recipient: Recipient) {
        openRecipientDialogLiveEvent.value = recipient
    }

    fun onIconColorClick() {
        selectColorLiveEvent.value = data.getIconColor()
    }

    fun onContactsClick(recipient: RecipientObservable) {
        selectContactLiveEvent.value = recipient
    }

    fun onRemoveRecipientClick(view: View, recipient: Recipient) {
        data.removeRecipient(recipient)
        removeRecipientLiveEvent.value = view
    }

    fun setIconColor(color: Int) {
        data.setIconColor(color)
    }

    fun isTemplateEdited(): Boolean {
        return original != data.model
    }

    fun addRecipient(recipient: Recipient?) {
        recipient?.let {
            data.addRecipient(it)
            selectContactLiveEvent.value?.model = it
        }
    }

    private fun fieldsChecker(){
        viewModelScope.launch{
            while (isActive) {
                data.notifyPropertyChanged(BR.fieldsFilled)
                delay(500)
            }
        }
    }
}