package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object TemplatesRepository {
    private val templatesDao = AppDatabase.INSTANCE!!.templateDao()
    private val groupsDao = AppDatabase.INSTANCE!!.templateGroupDao()

    val groups: LiveData<List<TemplateGroup>> = groupsDao.getAll()
    val templates: LiveData<List<Template>> = templatesDao.getAll()


    suspend fun saveGroup(item: TemplateGroup) = withContext(IO){
        val group = getGroupById(item.groupId)
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
            increaseGroupSize(item.groupId)
        }
        else templatesDao.update(item)
    }

    suspend fun updateTemplate(item: Template) = withContext(IO) {
        templatesDao.update(item)
    }

    suspend fun deleteTemplate(item: Template) = withContext(IO) {
        templatesDao.delete(item)
        decreaseGroupSize(item.groupId)
    }

    fun getTemplatesByGroupId(groupId: String): LiveData<List<Template>> {
        return templatesDao.getByGroupId(groupId)
    }

    private suspend fun increaseGroupSize(id: String){
        getGroupById(id)?.let {
                it.size++
                groupsDao.update(it)
        }
    }

    private suspend fun decreaseGroupSize(id: String) {
        getGroupById(id)?.let {
                it.size++
                groupsDao.update(it)
        }
    }

    suspend fun getGroupById(id: String) = withContext(IO){
        groupsDao.get(id)
    }

    suspend fun getTemplateById(id: String) = withContext(IO){
       templatesDao.get(id)
    }

    suspend fun updateTemplates() = withContext(IO) {
        templates.value?.toTypedArray()?.let {
            templatesDao.update(*it)
        }
    }
}