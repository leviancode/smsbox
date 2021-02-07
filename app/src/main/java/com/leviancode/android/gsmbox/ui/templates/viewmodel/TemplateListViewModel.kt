package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val templates: LiveData<List<Template>> = repository.templates
    val createTemplateLiveEvent = SingleLiveEvent<Unit>()
    val sendMessageLiveEvent = SingleLiveEvent<Template>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, Template>>()

    fun addTemplate(template: Template) {
        viewModelScope.launch {
            repository.addTemplate(template)
        }
    }

    fun deleteTemplate(template: Template) {
        viewModelScope.launch {
            repository.deleteTemplate(template)
        }
    }

    fun getGroupTemplates (groupId: String) = repository.getTemplatesByGroupId(groupId)

    fun onCreateTemplateClick(){
        createTemplateLiveEvent.call()
    }

    fun onSendMessage(template: TemplateObservable){
        sendMessageLiveEvent.value = template.data
    }

    fun onPopupMenuClick(view: View, item: TemplateObservable){
        popupMenuLiveEvent.value = Pair(view, item.data)
    }

    fun onFavoriteClick(template: TemplateObservable){
        template.setFavorite(!template.isFavorite())
        viewModelScope.launch {
            repository.updateTemplate(template.data)
        }
    }
}