package com.leviancode.android.gsmbox.ui.fragments.settings.placeholders

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.core.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.core.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.core.utils.SingleLiveEvent
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