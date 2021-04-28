package com.leviancode.android.gsmbox.ui.settings.placeholders

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaceholdersViewModel : ViewModel() {
    private val repository = PlaceholdersRepository
    val addPlaceholderEvent = SingleLiveEvent<Placeholder>()
    val onPopupMenuClickEvent = SingleLiveEvent<Pair<View, Placeholder>>()


    fun getPlaceholders(): LiveData<List<Placeholder>> = repository.getPlaceholders()


    fun onAddPlaceholderClick(){
        addPlaceholderEvent.value = repository.getNewPlaceholder()
    }

    fun onPopupMenuClick(view: View, placeholder: Placeholder){
        onPopupMenuClickEvent.value = view to placeholder
    }

    fun savePlaceholder(placeholder: Placeholder){
        placeholder.setName("#${placeholder.getName()}")
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