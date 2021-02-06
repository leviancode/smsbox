package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object TemplatesRepository {
    private val templatesDao = AppDatabase.INCTANCE!!.templateDao()
    private val groupsDao = AppDatabase.INCTANCE!!.templateGroupDao()

    val groups: LiveData<List<TemplateGroup>> = groupsDao.getAll()
    val templates: LiveData<List<Template>> = templatesDao.getAll()


    suspend fun addGroup(item: TemplateGroup) = withContext(IO){
        val group = getGroupById(item.groupId)
        if (group == null) groupsDao.insert(item)
        else groupsDao.update(item)
    }

    suspend fun updateGroup(item: TemplateGroup) = withContext(IO){
        groupsDao.update(item)
    }

    suspend fun removeGroup(item: TemplateGroup) = withContext(IO){
        groupsDao.delete(item)
    }

    suspend fun addTemplate(item: Template) = withContext(IO){
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

    suspend fun removeTemplate(item: Template) = withContext(IO) {
        templatesDao.delete(item)
        decreaseGroupSize(item.groupId)
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

    private fun getGroupById(id: String): TemplateGroup?{
        return groups.value?.find { it.groupId == id }
    }

    private fun getTemplateById(id: String): Template?{
        return templates.value?.find { it.templateId == id }
    }

    suspend fun updateTemplates() = withContext(IO) {
        templates.value?.toTypedArray()?.let {
            templatesDao.update(*it)
        }
    }
}