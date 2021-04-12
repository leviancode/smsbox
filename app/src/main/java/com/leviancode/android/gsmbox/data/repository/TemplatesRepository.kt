package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.TemplateAndRecipientRelationDao
import com.leviancode.android.gsmbox.data.dao.TemplateDao
import com.leviancode.android.gsmbox.data.dao.TemplateGroupDao
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateAndRecipientRelation
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object TemplatesRepository {
    private val templatesDao: TemplateDao
        get() = AppDatabase.instance.templateDao()
    private val groupsDao: TemplateGroupDao
        get() = AppDatabase.instance.templateGroupDao()

    private val templateWithRecipientsDao: TemplateAndRecipientRelationDao
        get() = AppDatabase.instance.templateAndRecipientsDao()

    fun getTemplateGroups() = groupsDao.getAllLiveData()
    fun getTemplateGroupNames() = groupsDao.getGroupNames()
    fun getGroupsWithTemplates() = groupsDao.getGroupsWithTemplates()
    fun getAllTemplates() = templatesDao.getAllLiveData()
    fun getTemplatesWithRecipients(groupId: Long) =
        templatesDao.getTemplatesWithRecipients(groupId)

    fun getFavoriteTemplates() = templatesDao.getFavoriteWithRecipients()

    fun getTemplateNames() = templatesDao.getTemplateNames()

    fun getTemplateNamesExclusiveById(id: Long) = templatesDao.getTemplateNamesExclusiveById(id)

    fun getNewTemplateWithRecipients(groupId: Long): TemplateWithRecipients {
        val gwr = RecipientsRepository.getNewGroupWithRecipients()
        return TemplateWithRecipients(
            Template(
                templateGroupId = groupId,
                recipientGroupId = gwr.group.recipientGroupId
            ), gwr)
    }

    suspend fun getTemplateWithRecipients(templateId: Long) = withContext(IO) {
        templatesDao.getTemplateWithRecipients(templateId)
    }

    fun getNewTemplateGroup() = TemplateGroup()

    fun getGroupWithTemplates(groupId: Long) = groupsDao.getGroupWithTemplates(groupId)

    suspend fun getTemplatesByRecipientGroupId(recipientGroupId: Long): List<Template> {
        return templatesDao.getByRecipientGroupId(recipientGroupId)
    }

    suspend fun saveGroup(item: TemplateGroup) = withContext(IO) {
        if (item.templateGroupId == 0L) groupsDao.insert(item)
        else {
            groupsDao.update(item)
            item.templateGroupId
        }
    }

    suspend fun saveTemplate(item: Template) = withContext(IO) {
        if (item.templateId == 0L) templatesDao.insert(item)
        else {
            templatesDao.update(item)
            item.templateId
        }
    }

    suspend fun deleteGroup(item: TemplateGroup) = withContext(IO) {
        groupsDao.delete(item)
    }

    suspend fun deleteTemplate(item: Template) = withContext(IO) {
        templatesDao.delete(item)
    }

    fun getTemplatesByGroupId(groupId: Long): LiveData<List<Template>> {
        return templatesDao.getByGroupId(groupId)
    }

    suspend fun getGroupById(id: Long) = withContext(IO) {
        groupsDao.get(id)
    }

    suspend fun getTemplateById(id: Long) = withContext(IO) {
        templatesDao.get(id)
    }

    suspend fun updateAllTemplates(list: List<Template>) = withContext(IO) {
        list.forEach { templatesDao.insert(it) }
    }

    suspend fun updateAllGroups(list: List<TemplateGroup>) = withContext(IO) {
        list.forEach { groupsDao.insert(it) }
    }

    suspend fun saveTemplateAndRecipientRelation(templateId: Long, recipientId: Long) = withContext(IO) {
        templateWithRecipientsDao.insert(
            TemplateAndRecipientRelation(templateId, recipientId)
        )
    }

    suspend fun deleteAllTemplateAndRecipientRelation(templateId: Long) = withContext(IO) {
        templateWithRecipientsDao.deleteByTemplateId(templateId)
    }

    suspend fun saveTemplateWithRecipients(data: TemplateWithRecipients) = withContext(IO) {
        log("save tmplt: $data")
        val groupId = RecipientsRepository.saveGroup(data.recipients.group)
        val templateId = saveTemplate(data.template.apply { recipientGroupId = groupId })
      //  RecipientsRepository.deleteGroupFromAllRecipients(groupId)
      //  deleteAllTemplateAndRecipientRelation(templateId)

        data.recipients.getRecipients().forEach { recipient ->
            val recipientId = RecipientsRepository.saveRecipient(recipient)
            RecipientsRepository.saveGroupAndRecipientRelation(groupId, recipientId)
            saveTemplateAndRecipientRelation(templateId, recipientId)
        }
    }

    suspend fun getTemplateByName(name: String): Template? {
        return templatesDao.getByName(name)
    }

    suspend fun getGroupByName(name: String): TemplateGroup? {
        return groupsDao.getByName(name)
    }
}