package com.leviancode.android.gsmbox.ui.templates.view.templates.list

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.managers.SmsManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val createTemplateLiveEvent = SingleLiveEvent<Unit>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, Template>>()

    val templates = repository.getAllTemplates()
    val favoriteTemplates = repository.getFavoriteTemplates()

    fun deleteTemplate(template: Template) {
        viewModelScope.launch {
            repository.deleteTemplate(template)
        }
    }

    fun loadTemplatesWithRecipients(groupId: Int) = repository.getTemplatesWithRecipients(groupId)

    fun onCreateTemplateClick() {
        createTemplateLiveEvent.call()
    }

    fun onSendMessage(view: View, model: TemplateWithRecipients) {
        viewModelScope.launch {
            SmsManager.sendSms(view.context, model)
        }
    }

    fun onPopupMenuClick(view: View, item: TemplateWithRecipients) {
        popupMenuLiveEvent.value = Pair(view, item.template)
    }

    fun onFavoriteClick(item: TemplateWithRecipients) {
        item.template.setFavorite(!item.template.isFavorite())
        viewModelScope.launch {
            repository.saveTemplate(item.template)
        }
    }

    fun updateOrder(list: List<TemplateWithRecipients>) {
        viewModelScope.launch {
            repository.insertAllTemplates(list.map { it.template })
        }
    }
}