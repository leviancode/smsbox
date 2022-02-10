package com.brainymobile.android.smsbox.ui.screens.recipients.groups.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.ui.entities.recipients.toRecipientGroupsUI
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RecipientGroupSelectListViewModel @Inject constructor(
    private val fetchRecipientGroupsUseCase: FetchRecipientGroupsUseCase,
) : ViewModel() {
    private val _selectedGroup = SingleLiveEvent<Int>()
    val selectedGroup: LiveData<Int> = _selectedGroup

    fun getGroups(groupId: Int): Flow<List<RecipientGroupUI>>
       = fetchRecipientGroupsUseCase.getAllObservable().map { list ->
           list.filter { it.id != groupId }.toRecipientGroupsUI()
        }

    fun onItemClick(item: RecipientGroupUI) {
        _selectedGroup.value = item.id
    }
}