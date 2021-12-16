package com.leviancode.android.gsmbox.ui.screens.templates.templates.list

import android.view.View
import androidx.lifecycle.*
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.DeleteTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.SaveTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.UpdateTemplateUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl.UpdateTemplateUseCaseImpl
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateUI
import com.leviancode.android.gsmbox.ui.entities.templates.toDomainTemplate
import com.leviancode.android.gsmbox.ui.entities.templates.toUITemplates
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.managers.SmsManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TemplateListViewModel(
    private val fetchUseCase: FetchTemplatesUseCase,
    private val deleteUseCase: DeleteTemplatesUseCase,
    private val updateUseCase: UpdateTemplateUseCase,
    private val smsManager: SmsManager
) : ViewModel() {
    val createTemplateEvent = SingleLiveEvent<Unit>()
    val popupMenuEvent = SingleLiveEvent<Pair<View, TemplateUI>>()

    val favoriteTemplates: LiveData<List<TemplateUI>>
        get() = fetchUseCase.getFavoritesObservable()
            .map { it.toUITemplates() }
            .asLiveData()

    fun getTemplates(groupId: Int) =
        fetchUseCase.getTemplatesByGroupIdObservable(groupId)
            .map { it.toUITemplates() }

    fun deleteTemplate(template: TemplateUI) {
        viewModelScope.launch {
            deleteUseCase.delete(template.toDomainTemplate())
        }
    }

    fun onCreateTemplateClick() {
        createTemplateEvent.call()
    }

    fun onSendMessage(view: View, model: TemplateUI) {
        viewModelScope.launch {
            smsManager.sendSms(view.context, model)
        }
    }

    fun onPopupMenuClick(view: View, item: TemplateUI) {
        popupMenuEvent.value = Pair(view, item)
    }

    fun onFavoriteClick(item: TemplateUI) {
        item.setFavorite(!item.isFavorite())
        viewModelScope.launch {
            updateUseCase.updateFavorite(item.id, item.isFavorite())
        }
    }
}