package com.leviancode.android.gsmbox.ui.screens.templates.templates.edit

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.SaveTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.UpdateTemplateUseCase
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import com.leviancode.android.gsmbox.ui.entities.placeholder.toUIPlaceholders
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientUI
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateUI
import com.leviancode.android.gsmbox.ui.entities.templates.toDomainTemplate
import com.leviancode.android.gsmbox.ui.entities.templates.toTemplateUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.VALIDATION_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class TemplateEditViewModel(
    private val fetchTemplatesUseCase: FetchTemplatesUseCase,
    private val saveTemplateUseCase: SaveTemplatesUseCase,
    private val updateTemplateUseCase: UpdateTemplateUseCase,
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val fetchPlaceholdersUseCase: FetchPlaceholdersUseCase
) : ViewModel() {
    private var data: TemplateUI = TemplateUI()
    private var original: TemplateUI? = null

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    val updateRecipientsEvent = SingleLiveEvent<List<RecipientUI>>()
    val addRecipientEvent = SingleLiveEvent<RecipientUI>()
    val saveRecipientEvent = SingleLiveEvent<RecipientUI>()

    val selectRecipientGroupEvent = SingleLiveEvent<RecipientGroupUI>()
    val selectRecipientEvent = SingleLiveEvent<Pair<RecipientUI, List<String>>>()
    val selectColorEvent = SingleLiveEvent<String>()
    val quitEvent = SingleLiveEvent<Unit>()

    val groupMode = MutableLiveData(false)
    var editMode: Boolean = false
        private set

    private var saved = false

    val recipientNumbers: LiveData<List<String>>
        get() = fetchRecipientsUseCase.getPhoneNumbersObservable().asLiveData()

    val placeholders: Flow<List<PlaceholderUI>>
        get() = fetchPlaceholdersUseCase.getPlaceholders().map { it.toUIPlaceholders() }

    init {
        validator()
    }

    fun loadTemplate(groupId: Int, templateId: Int = 0) = flow {
        editMode = templateId != 0
        if (original == null){
            if (editMode){
                fetchTemplatesUseCase.get(templateId)?.toTemplateUI()?.let {
                    data = it
                }
            }
            if (data.recipientGroup.recipients.isEmpty()) {
                data.recipientGroup.addRecipient(RecipientUI())
            }
            data.groupId = groupId
            original = data.copy()
        }
        groupMode.value = data.recipientGroup.isGroupNameNotNull()
        updateRecipientViews()
        emit(data)
    }

    fun onAddRecipientClick() {
        val newRecipient = RecipientUI()
        data.recipientGroup.addRecipient(newRecipient)
        addRecipientEvent.value = newRecipient
    }

    fun onRemoveRecipientClick(recipient: RecipientUI) {
        data.recipientGroup.removeRecipient(recipient)
    }

    fun onSaveRecipientClick(recipient: RecipientUI) {
        saveRecipientEvent.value = recipient
    }

    private fun updateRecipientViews() {
        updateRecipientsEvent.value = data.recipientGroup.recipients
    }

    fun onSelectRecipientGroupsClick() {
        selectRecipientGroupEvent.value = data.recipientGroup
    }

    fun onSelectRecipientClick(recipient: RecipientUI) {
        data.recipientGroup.let { recipientGroup ->
            selectRecipientEvent.value =
                recipient to (recipientGroup.recipients
                    .filter { it.getPhoneNumber() != recipient.getPhoneNumber() }
                    .map { it.getPhoneNumber() })
        }

    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (templateNameValidate()){
                if (groupMode.value == false){
                    if (data.recipientGroup.getName() != null){
                        data.recipientGroup.setName(null)
                        data.recipientGroup.id = 0
                    }
                }
                if (editMode){
                    updateTemplateUseCase.update(data.toDomainTemplate())
                } else {
                    saveTemplateUseCase.save(data.toDomainTemplate())
                }
                saved = true
                quitEvent.call()
            }
        }
    }

    fun onIconColorClick() {
        selectColorEvent.value = data.getIconColor()
    }

    fun setIconColor(color: String) {
        data.setIconColor(color)
    }

    fun isDataSavedOrNotChanged(): Boolean = saved || original == data

    fun updateRecipientGroup(groupId: Int) {
        viewModelScope.launch {
            fetchRecipientGroupsUseCase.getById(groupId)?.toRecipientGroupUI()?.also {
                data.recipientGroup.updateGroup(it)
                updateRecipientViews()
            }
        }
    }

    fun updateRecipient(old: RecipientUI, savedRecipientId: Int) {
        viewModelScope.launch {
            val savedRecipient = fetchRecipientsUseCase.getById(savedRecipientId)?.toRecipientUI()
            if (savedRecipient != null){
                old.setPhoneNumber(savedRecipient.getPhoneNumber())
                old.setName(savedRecipient.getName())
                old.id = savedRecipient.id
            }
        }
    }

    fun updateRecipient(old: RecipientUI, new: RecipientUI) {
        old.setPhoneNumber(new.getPhoneNumber())
        old.setName(new.getName())
        old.id = new.id
    }

    private fun validator() = viewModelScope.launch {
        while (isActive){
            data.nameUnique = templateNameValidate()
            phoneNumbersValidate()
            delay(VALIDATION_DELAY)
        }
    }

    private suspend fun phoneNumbersValidate() {
        data.recipientGroup.recipients.forEach { recipient ->
            val recipientFound = fetchRecipientsUseCase.getByPhoneNumber(recipient.getPhoneNumber())
            recipient.phoneNumberUnique = recipientFound?.name == null
        }
    }

    private suspend fun templateNameValidate(): Boolean {
        return data.let { template ->
            val templateName = template.getName()
            val foundTemplate = fetchTemplatesUseCase.getByName(templateName)
            if (foundTemplate != null && foundTemplate.id != template.id){
                _nameError.value = R.string.err_unique_name
                false
            } else {
                _nameError.value = null
                true
            }
        }
    }
}
