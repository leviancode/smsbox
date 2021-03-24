package com.leviancode.android.gsmbox.ui.templates.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val repository = TemplatesRepository
    val templates: LiveData<List<Template>> = repository.templates
    val favoriteTemplates: LiveData<List<Template>> = templates.map { list ->
        list.filter { it.isFavorite() }
    }
    val createTemplateLiveEvent = SingleLiveEvent<Template>()
    val sendMessageLiveEvent = SingleLiveEvent<Template>()
    val popupMenuLiveEvent = SingleLiveEvent<Pair<View, Template>>()

    fun deleteTemplate(template: Template) {
        viewModelScope.launch {
            repository.deleteTemplate(template)
        }
    }

    fun getTemplatesByGroupId(groupId: String) = repository.getTemplatesByGroupId(groupId)

    /*fun getGroupWithTemplates(groupId: String) = repository.groupsWithTemplates.map { list ->
        list.first { it.group.groupId == groupId }
    }*/
    fun getGroupWithTemplates(groupId: String) = repository.getGroupWithTemplates(groupId)

    fun onCreateTemplateClick(){
        createTemplateLiveEvent.value = Template()
    }

    fun onSendMessage(template: Template){
        sendMessageLiveEvent.value = template
    }

    fun onPopupMenuClick(view: View, item: Template){
        popupMenuLiveEvent.value = Pair(view, item)
    }

    fun onFavoriteClick(template: Template){
        template.setFavorite(!template.isFavorite())
        viewModelScope.launch {
            repository.updateTemplate(template)
        }
    }

    fun updateAll(list: List<Template>) {
        viewModelScope.launch {
            repository.updateAllTemplates(list)
        }
    }
}