package com.leviancode.android.gsmbox.core.data.repository

import com.leviancode.android.gsmbox.core.data.dao.templates.TemplateDao
import com.leviancode.android.gsmbox.core.data.dao.templates.TemplateGroupDao
import com.leviancode.android.gsmbox.core.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.core.data.model.templates.Template
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateWithRecipients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

object TemplatesRepository {
    private  val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private  val IOContext = scope.coroutineContext

    private val templatesDao: TemplateDao = AppDatabase.instance.templateDao()
    private val groupsDao: TemplateGroupDao = AppDatabase.instance.templateGroupDao()

    fun getNewTemplateGroup() = TemplateGroup()
    fun getTemplateGroups() = groupsDao.getAllLiveData()
    fun getTemplateGroupNames() = groupsDao.getGroupNames()
    fun getGroupsWithTemplates() = groupsDao.getGroupsWithTemplates()
    fun getAllTemplates() = templatesDao.getAllLiveData()

    suspend fun getGroupById(id: Int) = groupsDao.get(id)

    suspend fun getTemplateByName(name: String) = templatesDao.getByName(name)

    suspend fun getGroupByName(name: String) = groupsDao.getByName(name)

    fun getTemplatesWithRecipients(groupId: Int) =
        templatesDao.getTemplatesWithRecipients(groupId)

    fun getFavoriteTemplates() = templatesDao.getFavoriteWithRecipients()

    fun getTemplateNamesExclusiveById(id: Int) = templatesDao.getTemplateNamesExclusiveById(id)

    fun getNewTemplateWithRecipients(groupId: Int): TemplateWithRecipients {
        val gwr = RecipientsRepository.getNewGroupWithRecipients()
        return TemplateWithRecipients(
            Template(templateGroupId = groupId), gwr)
    }

    suspend fun getTemplateWithRecipients(templateId: Int) = withContext(IOContext){
        templatesDao.getTemplateWithRecipients(templateId)
    }

    suspend fun saveGroup(item: TemplateGroup): Int = withContext(IOContext){
        groupsDao.upsert(item)
    }

    suspend fun saveTemplate(item: Template): Int = templatesDao.upsert(item)

    suspend fun saveTemplateWithRecipients(data: TemplateWithRecipients) = withContext(IOContext){
        data.recipients?.let {
            val groupId = RecipientsRepository.saveGroupWithRecipients(it)
            data.template.recipientGroupId = groupId
        }
        templatesDao.upsert(data.template)
    }

    suspend fun insertAllTemplates(list: List<Template>) = withContext(IOContext){
        templatesDao.insertAll(list)
    }

    suspend fun insertAllGroups(list: List<TemplateGroup>) = withContext(IOContext){
        groupsDao.insertAll(list)
    }

    suspend fun deleteGroup(item: TemplateGroup) = withContext(IOContext){
        groupsDao.delete(item)
    }

    suspend fun deleteTemplate(item: Template) = withContext(IOContext){
        templatesDao.delete(item)
    }

    suspend fun deleteGroupWithTemplates(item: GroupWithTemplates) = withContext(IOContext){
        item.templates.forEach {  template ->
            template.recipientGroupId?.let { RecipientsRepository.deleteGroupById(it) }
        }
        deleteGroup(item.group)
    }

    suspend fun replaceGroupIds(firstId: Int, secondId: Int) = withContext(IOContext){
        val tempId = Int.MAX_VALUE
        groupsDao.updateId(firstId, tempId)
        groupsDao.updateId(secondId, firstId)
        groupsDao.updateId(tempId, secondId)
    }

}


