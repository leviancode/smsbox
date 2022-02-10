package com.brainymobile.android.smsbox.ui.screens.settings.placeholders.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.placeholders.SavePlaceholdersUseCase
import com.brainymobile.android.smsbox.ui.entities.placeholder.PlaceholderUI
import com.brainymobile.android.smsbox.ui.entities.placeholder.toDomainPlaceholder
import com.brainymobile.android.smsbox.ui.entities.placeholder.toPlaceholderUI
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import com.brainymobile.android.smsbox.utils.VALIDATION_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceholderEditViewModel @Inject constructor(
    private val fetchPlaceholdersUseCase: FetchPlaceholdersUseCase,
    private val savePlaceholdersUseCase: SavePlaceholdersUseCase,
) : ViewModel() {
    private var data: PlaceholderUI = PlaceholderUI()
    private val _keyValidationError = MediatorLiveData<Int?>()
    val keyValidationError: LiveData<Int?> = _keyValidationError

    private val _placeholderSavedEvent = SingleLiveEvent<Unit>()
    val placeholderSavedEvent: LiveData<Unit> = _placeholderSavedEvent

    init {
        launchValidator()
    }

    fun load(id: Int) = flow{
        if (id != 0){
            fetchPlaceholdersUseCase.getById(id)?.let { placeholder ->
                data = placeholder.toPlaceholderUI()
            }
        }
        emit(data)
    }

    fun savePlaceholder(){
        viewModelScope.launch {
            if (validate()){
                savePlaceholdersUseCase.save(data.toDomainPlaceholder())
                _placeholderSavedEvent.call()
            }
        }
    }

    private fun launchValidator(){
        viewModelScope.launch {
            while(isActive){
                data.nameUnique = validate()
                delay(VALIDATION_DELAY)
            }
        }
    }

    private suspend fun validate(): Boolean{
        val placeholder = fetchPlaceholdersUseCase.getByName(data.getName())
        val valid = placeholder == null || placeholder.id == data.id
        return if (valid) {
            _keyValidationError.value = null
            true
        } else {
            _keyValidationError.value = R.string.err_unique_name
            false
        }
    }
}