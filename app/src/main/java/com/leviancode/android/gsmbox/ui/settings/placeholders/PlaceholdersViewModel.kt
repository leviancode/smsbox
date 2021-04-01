package com.leviancode.android.gsmbox.ui.settings.placeholders

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaceholdersViewModel : ViewModel() {
    private val repository = PlaceholdersRepository
    val placeholders: LiveData<List<Placeholder>> = repository.data
    val addPlaceholderEvent = SingleLiveEvent<Placeholder>()
    val onPopupMenuClickEvent = SingleLiveEvent<Pair<View, Placeholder>>()

    fun keysWithoutCurrentPlaceholder(id: String) = placeholders.map { list ->
        list.filter { it.placeholderId != id }
            .map { it.getKeyword().substring(1)}
    }

    fun onAddPlaceholderClick(){
        addPlaceholderEvent.value = repository.getNewPlaceholder()
    }

    fun onPopupMenuClick(view: View, placeholder: Placeholder){
        onPopupMenuClickEvent.value = view to placeholder
    }

    fun savePlaceholder(placeholder: Placeholder){
        placeholder.setKeyword("#${placeholder.getKeyword()}")
        viewModelScope.launch {
            repository.savePlaceholder(placeholder)
        }
    }

    fun deletePlaceholder(placeholder: Placeholder) {
        viewModelScope.launch {
            repository.deletePlaceholder(placeholder)
        }
    }
}