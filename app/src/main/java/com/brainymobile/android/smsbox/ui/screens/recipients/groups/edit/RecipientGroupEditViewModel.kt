package com.brainymobile.android.smsbox.ui.screens.recipients.groups.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.SaveRecipientGroupsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.ui.entities.recipients.toDomainRecipientGroup
import com.brainymobile.android.smsbox.ui.entities.recipients.toRecipientGroupUI
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipientGroupEditViewModel @Inject constructor(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val saveRecipientGroupsUseCase: SaveRecipientGroupsUseCase
) : ViewModel() {
    val selectColorEvent = SingleLiveEvent<RecipientGroupUI>()
    val closeDialogEvent = SingleLiveEvent<Int>()
    val nameValidation = SingleLiveEvent<Int>()

    private var data = RecipientGroupUI()

    fun getNamesWithoutCurrent(id: Int) = fetchRecipientGroupsUseCase.getAllObservable().map { list ->
        list.filter { it.id != id }.map { it.name }
    }.asLiveData()

    fun loadGroup(id: Int) = flow {
        if (id != 0){
            fetchRecipientGroupsUseCase.getById(id)?.toRecipientGroupUI()?.let {
                data = it
            }
        }
        emit(data)
    }

    fun onSaveClick(item: RecipientGroupUI) {
        viewModelScope.launch {
            if (checkNameForUnique(item.getName())) {
                val savedId = saveRecipientGroupsUseCase.save(item.toDomainRecipientGroup())
                closeDialogEvent.value = savedId
            } else {
                nameValidation.value = R.string.err_unique_name
            }
        }
    }

    private suspend fun checkNameForUnique(name: String?): Boolean {
        name ?: return true
        return fetchRecipientGroupsUseCase.getByName(name) == null
    }

    fun onIconColorClick(item: RecipientGroupUI) {
        selectColorEvent.value = item
    }
}