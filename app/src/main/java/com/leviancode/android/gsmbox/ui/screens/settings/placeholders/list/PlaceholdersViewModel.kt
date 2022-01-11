package com.leviancode.android.gsmbox.ui.screens.settings.placeholders.list

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.domain.usecases.placeholders.DeletePlaceholdersUseCase
import com.leviancode.android.gsmbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import com.leviancode.android.gsmbox.ui.entities.placeholder.toUIPlaceholders
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaceholdersViewModel(
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