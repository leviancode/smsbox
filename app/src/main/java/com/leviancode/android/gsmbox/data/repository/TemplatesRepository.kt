package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.data.dao.TemplateDao
import com.leviancode.android.gsmbox.data.dao.TemplateGroupDao
import com.leviancode.android.gsmbox.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object TemplatesRepository {
    private val templatesDao: TemplateDao
        get() = AppDatabase.INSTANCE.templateDao()
    private val groupsDao: TemplateGroupDao
        get() = AppDatabase.INSTANCE.templateGroupDao()

    val groups: LiveData<List<TemplateGroup>>
        get() = groupsDao.getAllLiveData()
    val groupsWithTemplates: LiveData<List<GroupWithTemplates>>
        get() = groupsDao.getGroupsWithTemplates()
    val templates: LiveData<List<Template>>
        get() = templatesDao.getAllLiveData()


    suspend fun saveGroup(item: TemplateGroup) = withContext(IO){
        val group = getGroupById(item.templateGroupId)
        if (group == null) groupsDao.insert(item)
        else groupsDao.update(item)
    }

    suspend fun updateGroup(item: TemplateGroup) = withContext(IO){
        groupsDao.update(item)
    }

    suspend fun deleteGroup(item: TemplateGroup) = withContext(IO){
        groupsDao.delete(item)
    }

    suspend fun saveTemplate(item: Template) = withContext(IO){
        val template = getTemplateById(item.templateId)
        if (template == null)  {
            templatesDao.insert(item)
            increaseGroupSize(item.templateGroupId)
        }
        else templatesDao.update(item)
    }

    suspend fun updateTemplate(item: Template) = withContext(IO) {
        templatesDao.update(item)
    }

    suspend fun deleteTemplate(item: Template) = withContext(IO) {
        templatesDao.delete(item)
        decreaseGroupSize(item.templateGroupId)
    }

    private suspend fun increaseGroupSize(id: String){
        getGroupById(id)?.let {
                it.size++
                groupsDao.update(it)
        }
    }

    private suspend fun decreaseGroupSize(id: String) {
        getGroupById(id)?.let {
                it.size--
                groupsDao.update(it)
        }
    }

    fun getTemplatesByGroupId(groupId: String): LiveData<List<Template>> {
        return templatesDao.getByGroupId(groupId)
    }

    suspend fun getGroupById(id: String) = withContext(IO){
        groupsDao.get(id)
    }

    suspend fun getTemplateById(id: String) = withContext(IO){
       templatesDao.get(id)
    }

    suspend fun updateAllTemplates(list: List<Template>) = withContext(IO) {
        templatesDao.insert(*list.toTypedArray())
    }

    suspend fun updateAllGroups(list: List<TemplateGroup>) = withContext(IO) {
        groupsDao.insert(*list.toTypedArray())
    }

    suspend fun updateRecipientGroupsInAllTemplates() = withContext(IO) {
        templatesDao.getAll().filter { it.getRecipientGroup() != null }
            .forEach { template ->
                RecipientsRepository.getGroupWithRecipients(template.getRecipientGroupId())?.let {
                    template.setRecipients(it.recipients)
                }
                updateTemplate(template)
        }
    }

    fun getGroupWithTemplates(groupId: String) = groupsDao.getGroupWithTemplates(groupId)

    fun getNewTemplate() = Template()
    fun getNewTemplateGroup() = TemplateGroup()
    suspend fun getTemplatesByRecipientGroupId(recipientGroupId: String): List<Template> {
        return templatesDao.getByRecipientGroupId(recipientGroupId)
    }
}