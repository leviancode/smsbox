package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class EditableTemplateViewModel : ViewModel() {
    private val repository = TemplatesRepository
    private var original: Template? = null
    val recipientNameList = RecipientsRepository.recipients.map { list ->
        list.map { it.getPhoneNumber() }
    }
    private var groupRecipients = listOf<Recipient>()
    var data: Template = repository.getNewTemplate()
    var recipientGroupMode = false

    val addRecipientViewEvent = SingleLiveEvent<Recipient>()
    val removeRecipientViewEvent = SingleLiveEvent<View>()
    val removeAllRecipientViewsEvent = SingleLiveEvent<Unit>()
    val saveRecipientEvent = SingleLiveEvent<Recipient>()

    val selectRecipientGroupEvent = SingleLiveEvent<String>()
    val selectRecipientEvent = SingleLiveEvent<Recipient>()
    val selectColorEvent = SingleLiveEvent<Int>()
    val savedEvent = SingleLiveEvent<Unit>()

    init {
        fieldsChecker()
    }

    fun setTemplate(template: Template){
        data = template
        original = template.copy()
        if (template.getRecipients().isEmpty()) {
            onAddRecipientClick()
        }
    }

    private fun addRecipientView(recipient: Recipient) {
        addRecipientViewEvent.value = recipient
    }

    fun onAddRecipientClick(){
        RecipientsRepository.getNewRecipient().also {
            data.addRecipient(it)
            addRecipientView(it)
        }
    }

    fun onSelectRecipientGroupsClick(){
        selectRecipientGroupEvent.value = data.getRecipientGroupId()
    }

    fun onSelectRecipientClick(recipient: Recipient){
        selectRecipientEvent.value = recipient
    }

    fun onSaveClick(){
        if (groupRecipients != data.getRecipients()){
            data.setRecipientGroup(null)
        }
        viewModelScope.launch {
            repository.saveTemplate(data)
        }
        savedEvent.call()
    }
    
    fun onSaveRecipientClick(recipient: Recipient) {
        saveRecipientEvent.value = recipient
    }

    fun onIconColorClick() {
        selectColorEvent.value = data.getIconColor()
    }

    fun onRemoveRecipientClick(view: View, recipient: Recipient) {
        data.removeRecipient(recipient)
        removeRecipientViewEvent.value = view
    }

    fun setIconColor(color: Int) {
        data.setIconColor(color)
    }

    fun isTemplateEdited(): Boolean {
        return original != data
    }

    private fun fieldsChecker(){
        viewModelScope.launch{
            while (isActive) {
                data.notifyPropertyChanged(BR.fieldsFilled)
                delay(500)
            }
        }
    }

    fun setRecipientGroup(groupId: String) {
        removeAllRecipientViewsEvent.call()
        viewModelScope.launch {
            RecipientsRepository.getGroupWithRecipients(groupId)?.let {
                groupRecipients = getListClone(it.recipients)
                data.setRecipientGroup(it.group)
                data.setRecipients(it.recipients)
                data.getRecipients().forEach(::addRecipientView)
            }
        }
    }

    private fun getListClone(list: List<Recipient>): List<Recipient> {
        val result = mutableListOf<Recipient>()
        list.forEach { result.add(it.copy()) }
        return result
    }

    fun updateRecipient(old: Recipient, new: Recipient) {
        old.apply {
            recipientId = new.recipientId
            setRecipientName(new.getRecipientName())
            setPhoneNumber(new.getPhoneNumber())
        }
    }
}