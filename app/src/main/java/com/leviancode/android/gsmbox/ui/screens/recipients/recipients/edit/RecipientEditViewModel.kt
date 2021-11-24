package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.edit

import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.SaveRecipientsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.*
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.managers.ContactsManager
import kotlinx.coroutines.launch

class RecipientEditViewModel(
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val saveRecipientsUseCase: SaveRecipientsUseCase,
    private val contactsManager: ContactsManager
) : ViewModel() {
    private var original: RecipientWithGroupsUI? = null
    private var data: RecipientWithGroupsUI = RecipientWithGroupsUI()

    val recipient: RecipientUI
        get() = data.recipient

    val nameValidation = MutableLiveData<Int>()

    val quitEvent = SingleLiveEvent<Unit>()
    val selectContactEvent = SingleLiveEvent<Unit>()
    val addToGroupEvent = SingleLiveEvent<IntArray>()
    val removeGroupEvent = SingleLiveEvent<View>()
    val addGroupViewsEvent = SingleLiveEvent<List<RecipientGroupUI>>()

    private var _saveFromTemplateMode = MutableLiveData(false)
    var saveFromTemplateMode: LiveData<Boolean> = _saveFromTemplateMode

    private var saved = false

    val isSaveFromTemplateMode: Boolean
        get() = saveFromTemplateMode.value ?: false

    fun setSaveFromTemplateMode(value: Boolean){
        _saveFromTemplateMode.value = value
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (validate()){
                data.toDomainRecipientWithGroups().let {
                    saveRecipientsUseCase.save(it)
                    saved = true
                    quitEvent.call()
                }
            }
        }

    }

    fun loadRecipient(recipientId: Int, phoneNumber: String? = "") = liveData {
        if (recipientId != 0) {
            fetchRecipientsUseCase.getRecipientWithGroupsById(recipientId)
                ?.toUIRecipientWithGroups()?.let {
                data = it
                addGroupViewsEvent.value = it.groups
            }
        } else if (!phoneNumber.isNullOrBlank()){
            data.recipient.setPhoneNumber(phoneNumber)
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
        addToGroupEvent.value = data.getGroupsIds().toIntArray()
    }

    fun onContactsClick() {
        selectContactEvent.call()
    }

    fun onRemoveGroupClick(view: View, group: RecipientGroupUI) {
        data.removeGroup(group)
        removeGroupEvent.value = view
    }

    fun isDataSavedOrNotChanged() = saved || original == data

    fun setGroups(ids: List<Int>) {
        viewModelScope.launch {
            data.groups =
                fetchRecipientGroupsUseCase.getByIds(ids).toRecipientGroupsUI().toMutableList()
            addGroupViewsEvent.value = data.groups
        }
    }

    fun addContact(uri: Uri){
        contactsManager.parseUri(uri)?.let {
            setRecipient(it)
        }
    }

    private suspend fun validate(): Boolean {
        val name = data.recipient.getName() ?: return false
        val found = fetchRecipientsUseCase.getByName(name)
        if (found != null && found.id != data.recipient.id){
            nameValidation.value = R.string.err_unique_name
            return false
        }
        return true
    }
}