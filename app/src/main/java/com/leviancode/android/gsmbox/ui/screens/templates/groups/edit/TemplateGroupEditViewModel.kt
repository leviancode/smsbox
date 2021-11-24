package com.leviancode.android.gsmbox.ui.screens.templates.groups.edit

import androidx.lifecycle.*
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.SaveTemplateGroupUseCase
import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateGroupUI
import com.leviancode.android.gsmbox.ui.entities.templates.toDomainTemplateGroup
import com.leviancode.android.gsmbox.ui.entities.templates.toTemplateGroupUI
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateGroupEditViewModel(
    private val saveUseCase: SaveTemplateGroupUseCase,
    private val fetchUseCae: FetchTemplateGroupsUseCase
) : ViewModel() {
    private var data: TemplateGroupUI = TemplateGroupUI()
    private var original: TemplateGroupUI? = null
    private var saved = false

    val nameValidation = MutableLiveData<Int>()

    val chooseColorEvent = SingleLiveEvent<String>()
    val quitEvent = SingleLiveEvent<Unit>()


    fun loadGroup(id: Int) = liveData {
        viewModelScope.launch {
            if (id != 0) {
                fetchUseCae.getById(id)?.let { group ->
                    data = group.toTemplateGroupUI()
                }
            }
            original = data.copy()
            emit(data)
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val valid = validation()
            if (valid) {
                saveUseCase.save(data.toDomainTemplateGroup())
                saved = true
                quitEvent.call()
            }
        }
    }

    private suspend fun validation(): Boolean {
        val found = fetchUseCae.getByName(data.getName())
        return found == null || found.id == data.id
    }

    fun onIconColorClick() {
        chooseColorEvent.value = data.getIconColor()
    }

    fun setIconColor(color: String) {
        data.setIconColor(color)
    }

    fun isDataSavedOrNotChanged() = saved || data == original
}