package com.brainymobile.android.smsbox.ui.screens.settings.placeholders.list

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.domain.usecases.placeholders.DeletePlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.brainymobile.android.smsbox.ui.entities.placeholder.PlaceholderUI
import com.brainymobile.android.smsbox.ui.entities.placeholder.toUIPlaceholders
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceholdersViewModel @Inject constructor(
    private val fetchPlaceholdersUseCase: FetchPlaceholdersUseCase,
    private val deletePlaceholdersUseCase: DeletePlaceholdersUseCase
) : ViewModel() {
    val addPlaceholderEvent = SingleLiveEvent<Unit>()
    val onPopupMenuClickEvent = SingleLiveEvent<Pair<View, Int>>()

    val placeholders: Flow<List<PlaceholderUI>>
        get() = fetchPlaceholdersUseCase.getPlaceholders().map { it.toUIPlaceholders() }


    fun onAddPlaceholderClick(){
        addPlaceholderEvent.call()
    }

    fun onPopupMenuClick(view: View, placeholder: PlaceholderUI){
        onPopupMenuClickEvent.value = view to placeholder.id
    }

    fun deletePlaceholder(id: Int) {
        viewModelScope.launch {
            deletePlaceholdersUseCase.delete(id)
        }
    }
}