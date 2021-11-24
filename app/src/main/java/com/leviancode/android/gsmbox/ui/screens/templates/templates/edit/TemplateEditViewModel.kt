package com.leviancode.android.gsmbox.ui.screens.templates.templates.edit

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.SaveTemplatesUseCase
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import com.leviancode.android.gsmbox.ui.entities.placeholder.toUIPlaceholders
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateUI
import com.leviancode.android.gsmbox.ui.entities.templates.toDomainTemplate
import com.leviancode.android.gsmbox.ui.entities.templates.toTemplateUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class TemplateEditViewModel(
    private val fetchTemplatesUseCase: FetchTemplatesUseCase,
    private val saveTemplateUseCase: SaveTemplatesUseCase,
    private val fetchRecipientsUseCase: FetchRecipientsUseCase,
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val fetchPlaceholdersUseCase: FetchPlaceholdersUseCase
) : ViewModel() {
    private val validationJob: Job
    private val _data = MutableLiveData(TemplateUI())
    val data: LiveData<TemplateUI> = _data

    private var original: TemplateUI? = null

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    private val _fieldsCorrect = MutableLiveData(false)
    val fieldsCorrect: LiveData<Boolean> = _fieldsCorrect

    val addSingleRecipientViewEvent = SingleLiveEvent<RecipientUI>()
    val addManyRecipientsViewEvent = SingleLiveEvent<List<RecipientUI>>()
    val removeRecipientViewEvent = SingleLiveEvent<View>()
    val removeAllRecipientViewsEvent = SingleLiveEvent<Unit>()
    val saveRecipientEvent = SingleLiveEvent<RecipientUI>()

    val selectRecipientGroupEvent = SingleLiveEvent<RecipientGroupUI>()
    val selectRecipientEvent = SingleLiveEvent<Pair<RecipientUI, List<String>>>()
    val selectColorEvent = SingleLiveEvent<String>()
    val quitEvent = SingleLiveEvent<Unit>()

    var recipientGroupMode = false
    private var saved = false

    val recipientNumbers: LiveData<List<String>>
        get() = fetchRecipientsUseCase.getPhoneNumbersObservable().asLiveData()

    val placeholders: LiveData<List<PlaceholderUI>>
        get() = fetchPlaceholdersUseCase.getPlaceholders().map { it.toUIPlaceholders() }.asLiveData()

    init {
        validationJob = autoCheckFields()
    }

    fun loadTemplate(id: Int) {
        viewModelScope.launch {
            fetchTemplatesUseCase.get(id)?.let {
                val template = it.toTemplateUI()
                _data.value = template
                if (it.recipientGroup.recipients.isEmpty()) {
                    _data.value?.recipientGroup?.addRecipient(RecipientUI())
                }
                original = template.copy()
                addManyRecipientsViewEvent.postValue(data.value?.recipientGroup!!.recipients)
            }
        }
    }

    fun createEmptyTemplate(groupId: Int) {
        _data.value?.groupId = groupId
        onAddRecipientClick()
    }

    fun onAddRecipientClick() {
        data.value?.let {
            val newRecipient = RecipientUI()
            it.recipientGroup.addRecipient(newRecipient)
            addRecipientView(newRecipient)
        }
    }

    private fun addRecipientView(recipient: RecipientUI) {
        addSingleRecipientViewEvent.value = recipient
    }

    fun onSelectRecipientGroupsClick() {
        selectRecipientGroupEvent.value = data.value?.recipientGroup
    }

    fun onSelectRecipientClick(recipient: RecipientUI) {
        data.value?.recipientGroup?.let { recipientGroup ->
            selectRecipientEvent.value =
                recipient to (recipientGroup.recipients
                    .filter { it.getPhoneNumber() != recipient.getPhoneNumber() }
                    .map { it.getPhoneNumber() })
        }

    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (templateNameValidate()){
                data.value?.let { saveTemplateUseCase.save(it.toDomainTemplate()) }
                saved = true
                quitEvent.call()
            }
        }
    }

    fun onSaveRecipientClick(recipient: RecipientUI) {
        saveRecipientEvent.value = recipient
    }

    fun onIconColorClick() {
        data.value?.let {
            selectColorEvent.value = it.getIconColor()
        }
    }

    fun onRemoveRecipientClick(view: View, recipient: RecipientUI) {
        data.value?.recipientGroup?.removeRecipient(recipient)
        removeRecipientViewEvent.value = view
    }

    fun setIconColor(color: String) {
        data.value?.setIconColor(color)
    }

    fun isDataSavedOrNotChanged(): Boolean = saved || original == data.value

    fun updateRecipientGroup(groupId: Int) {
        viewModelScope.launch {
            fetchRecipientGroupsUseCase.getById(groupId)?.toRecipientGroupUI()?.also {
                data.value?.recipientGroup?.updateGroup(it)
                removeAllRecipientViewsEvent.call()
                addManyRecipientsViewEvent.value = it.recipients
            }
        }

    }

    fun updateRecipient(old: RecipientUI, new: RecipientUI) {
        old.setPhoneNumber(new.getPhoneNumber())
        old.setName(new.getPhoneNumber())
        old.id = new.id
    }

    private fun autoCheckFields() = viewModelScope.launch {
        while (isActive){
            templateNameValidate()
            recipientNumbersUniqueCheck()
            delay(500)
        }
    }

    private suspend fun recipientNumbersUniqueCheck() {
        data.value?.recipientGroup?.recipients?.forEach { recipient ->
            recipient.phoneNumberUnique =
                fetchRecipientsUseCase.getByPhoneNumber(recipient.getPhoneNumber()) == null

        }
    }

    private suspend fun templateNameValidate(): Boolean {
        return data.value?.let { template ->
            val templateName = template.getName()
            val foundTemplate = fetchTemplatesUseCase.getByName(templateName)
            if (foundTemplate != null && foundTemplate.id != template.id){
                _nameError.value = R.string.err_unique_name
                false
            } else {
                _nameError.value = null
                true
            }
        } ?: false
    }

    override fun onCleared() {
        validationJob.cancel()
        super.onCleared()
    }
}
