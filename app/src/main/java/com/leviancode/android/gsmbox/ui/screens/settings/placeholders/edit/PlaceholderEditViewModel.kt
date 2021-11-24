package com.leviancode.android.gsmbox.ui.screens.settings.placeholders.edit

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.leviancode.android.gsmbox.domain.usecases.placeholders.SavePlaceholdersUseCase
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import com.leviancode.android.gsmbox.ui.entities.placeholder.toDomainPlaceholder
import com.leviancode.android.gsmbox.ui.entities.placeholder.toPlaceholderUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.VALIDATOR_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaceholderEditViewModel(
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
                delay(VALIDATOR_DELAY)
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