package com.brainymobile.android.smsbox.ui.screens.templates.templates.list

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.DeleteTemplatesUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.UpdateTemplateUseCase
import com.brainymobile.android.smsbox.ui.entities.templates.TemplateUI
import com.brainymobile.android.smsbox.ui.entities.templates.toDomainTemplate
import com.brainymobile.android.smsbox.ui.entities.templates.toDomainTemplates
import com.brainymobile.android.smsbox.ui.entities.templates.toUITemplates
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import com.brainymobile.android.smsbox.utils.managers.SmsManager
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

    fun updateAll(items: List<TemplateUI>) {
        viewModelScope.launch {
            updateUseCase.update(items.toDomainTemplates())
        }
    }
}