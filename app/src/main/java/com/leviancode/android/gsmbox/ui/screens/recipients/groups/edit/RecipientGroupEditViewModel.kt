package com.leviancode.android.gsmbox.ui.screens.recipients.groups.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.SaveRecipientGroupsUseCase
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.toDomainRecipientGroup
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipientGroupEditViewModel(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
    private val saveRecipientGroupsUseCase: SaveRecipientGroupsUseCase
) : ViewModel() {
    val selectColorEvent = SingleLiveEvent<RecipientGroupUI>()
    val closeDialogEvent = SingleLiveEvent<RecipientGroupUI>()
    val nameValidation = SingleLiveEvent<Int>()

    fun getNamesWithoutCurrent(id: Int) = fetchRecipientGroupsUseCase.getAll().map { list ->
        list.filter { it.id != id }.map { it.name }
    }.asLiveData()

    suspend fun loadGroup(id: Int) = fetchRecipientGroupsUseCase.getById(id)?.toRecipientGroupUI()

    fun onSaveClick(item: RecipientGroupUI) {
        viewModelScope.launch {
            if (checkNameForUnique(item.getName())) {
                val savedId = saveRecipientGroupsUseCase.save(item.toDomainRecipientGroup())
                closeDialogEvent.value = item.apply { id = savedId }
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