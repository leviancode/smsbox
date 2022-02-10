package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.edit

import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.SaveRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.UpdateRecipientsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.*
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import com.brainymobile.android.smsbox.utils.VALIDATION_DELAY
import com.brainymobile.android.smsbox.utils.managers.ContactsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipientEditViewModel @Inject constructor(
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val saveRecipientsUseCase: SaveRecipientsUseCase,
    private val updateRecipientsUseCase: UpdateRecipientsUseCase,
    private val contactsManager: ContactsManager
) : ViewModel() {
    private var data: RecipientWithGroupsUI = RecipientWithGroupsUI()
    private var original: RecipientWithGroupsUI? = null

    private val recipient: RecipientUI
        get() = data.recipient

    init {
        validator()
    }

    val nameValidation = MutableLiveData<Int?>()
    val phoneNumberValidation = MutableLiveData<Int?>()

    private val _recipientSavedEvent = SingleLiveEvent<Int>()
    val recipientSavedEvent: LiveData<Int> = _recipientSavedEvent

    val selectContactEvent = SingleLiveEvent<Unit>()
    val addToGroupEvent = SingleLiveEvent<List<Int>>()
    val removeGroupEvent = SingleLiveEvent<View>()
    val addGroupViewsEvent = SingleLiveEvent<List<RecipientGroupUI>>()

    private var _saveFromTemplateMode = MutableLiveData(false)
    var saveFromTemplateMode: LiveData<Boolean> = _saveFromTemplateMode

    private var saved = false
    private var editMode = false

    val isSaveFromTemplate: Boolean
        get() = saveFromTemplateMode.value ?: false

    fun onSaveClick() {
        viewModelScope.launch {
            if (nameValidate()){
                data.toDomainRecipientWithGroups().let {
                    val id = if (editMode){
                        updateRecipientsUseCase.update(it)
                    } else {
                        saveRecipientsUseCase.save(it)
                    }
                    saved = true
                    _recipientSavedEvent.value = id
                }
            }
        }
    }

    fun loadRecipient(recipientId: Int, phoneNumber: String?, recipientName: String?) = flow {
        editMode = recipientId != 0
        if (editMode) {
            fetchRecipientsUseCase.getRecipientWithGroupsById(recipientId)
                ?.toUIRecipientWithGroups()?.let {
                data = it
                addGroupViewsEvent.value = it.namedGroups
            }
        } else if (!phoneNumber.isNullOrBlank()){
            _saveFromTemplateMode.value = true
            data.recipient.setPhoneNumber(phoneNumber)
            if (!recipientName.isNullOrBlank()){
                data.recipient.setName(recipientName)
            }
        }
        original = data.copy()
        emit(data.recipient)
    }

    fun setPhoneNumber(phoneNumber: String) {
        data.recipient.setPhoneNumber(phoneNumber)
    }

    fun setRecipient(recipient: RecipientUI) {
        data.recipient.apply {
            setName(recipient.getName())
            setPhoneNumber(recipient.getPhoneNumber())
        }
    }

    fun onAddToGroupClick() {
        addToGroupEvent.value = data.getGroupsIds()
    }

    fun onContactsClick() {
        selectContactEvent.call()
    }

    fun onRemoveGroupClick(view: View, group: RecipientGroupUI) {
        data.removeGroup(group)
        removeGroupEvent.value = view
    }

    fun isDataSavedOrNotChanged() = saved || original == data

    fun setGroups(groups: List<RecipientGroupUI>) {
        data.groups = groups.toMutableList().apply {
            addAll(data.groups.filter { !it.isGroupNameNotNull() })
        }
        addGroupViewsEvent.value = data.namedGroups
    }

    fun addContact(uri: Uri){
        contactsManager.parseUri(uri)?.let {
            setRecipient(it)
        }
    }

    private fun validator(){
        viewModelScope.launch {
            while (isActive){
                recipient.nameUnique = nameValidate()
                recipient.phoneNumberUnique = phoneNumberValidate()
                delay(VALIDATION_DELAY)
            }
        }
    }

    private suspend fun nameValidate(): Boolean {
        val name = data.recipient.getName() ?: return false
        val found = fetchRecipientsUseCase.getByName(name)
        return if (found != null && found.id != data.recipient.id){
            nameValidation.value = R.string.err_unique_name
            false
        } else {
            nameValidation.value = null
            true
        }
    }

    private suspend fun phoneNumberValidate(): Boolean {
        val phone = data.recipient.getPhoneNumber()
        val found = fetchRecipientsUseCase.getByPhoneNumber(phone)
        return if (found != null && found.id != data.recipient.id && found.name != null){
            phoneNumberValidation.value = R.string.err_unique_phone
            false
        } else {
            phoneNumberValidation.value = null
            true
        }
    }
}