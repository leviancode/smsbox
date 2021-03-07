package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val templates: LiveData<List<Template>> = repository.templates
    val createTemplateLiveEvent = SingleLiveEvent<Template>()
    val sendMessageLiveEvent = SingleLiveEvent<Template>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, Template>>()

    fun deleteTemplate(template: Template) {
        viewModelScope.launch {
            repository.deleteTemplate(template)
        }
    }

    fun getGroupTemplates (groupId: String) = repository.getTemplatesByGroupId(groupId)

    fun onCreateTemplateClick(){
        createTemplateLiveEvent.value = Template()
    }

    fun onSendMessage(template: Template){
        sendMessageLiveEvent.value = template
    }

    fun onPopupMenuClick(view: View, item: Template){
        popupMenuLiveEvent.value = Pair(view, item)
    }

    fun onFavoriteClick(template: TemplateObservable){
        template.setFavorite(!template.isFavorite())
        viewModelScope.launch {
            repository.updateTemplate(template.model)
        }
    }

    fun updateAll(list: List<Template>) {
        viewModelScope.launch {
            repository.updateAllTemplates(list)
        }
    }
}