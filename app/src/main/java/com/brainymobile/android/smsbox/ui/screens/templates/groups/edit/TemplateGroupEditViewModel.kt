package com.brainymobile.android.smsbox.ui.screens.templates.groups.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.SaveTemplateGroupUseCase
import com.brainymobile.android.smsbox.ui.entities.templates.TemplateGroupUI
import com.brainymobile.android.smsbox.ui.entities.templates.toDomainTemplateGroup
import com.brainymobile.android.smsbox.ui.entities.templates.toTemplateGroupUI
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import com.brainymobile.android.smsbox.utils.VALIDATION_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TemplateGroupEditViewModel(
    private val saveUseCase: SaveTemplateGroupUseCase,
    private val fetchUseCae: FetchTemplateGroupsUseCase
) : ViewModel() {
    private var data: TemplateGroupUI = TemplateGroupUI()
    private var original: TemplateGroupUI? = null
    private var saved = false

    private val _nameValidationError = MutableLiveData<Int?>()
    val nameValidationError: LiveData<Int?> = _nameValidationError

    private val _chooseColorEvent = SingleLiveEvent<String>()
    val chooseColorEvent: LiveData<String> = _chooseColorEvent

    private val _quitEvent = SingleLiveEvent<Unit>()
    val quitEvent: LiveData<Unit> = _quitEvent

    init {
        validator()
    }

    fun loadGroup(id: Int) = flow {
        if (id != 0) {
            fetchUseCae.getById(id)?.let { group ->
                data = group.toTemplateGroupUI()
            }
        }
        original = data.copy()
        emit(data)
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (validate()) {
                saveUseCase.save(data.toDomainTemplateGroup())
                saved = true
                _quitEvent.call()
            }
        }
    }

    private fun validator(){
        viewModelScope.launch {
            while (isActive){
                data.nameUnique = validate()
                delay(VALIDATION_DELAY)
            }
        }
    }

    private suspend fun validate(): Boolean {
        val found = fetchUseCae.getByName(data.getName())
        val nameValid = found == null || found.id == data.id
        return if (nameValid){
            _nameValidationError.value = null
            true
        } else {
            _nameValidationError.value = R.string.err_unique_name
            false
        }
    }

    fun onIconColorClick() {
        _chooseColorEvent.value = data.getIconColor()
    }

    fun setIconColor(color: String) {
        data.setIconColor(color)
    }

    fun isDataSavedOrNotChanged() = saved || data == original
}