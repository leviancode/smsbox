package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.utils.*
import kotlinx.coroutines.*

object TemplatesRepository {
    private val templateDao = AppDatabase.INCTANCE!!.templateDao()
    private val templateGroupDao = AppDatabase.INCTANCE!!.templateGroupDao()

    private val _groups = MutableLiveData<List<TemplateGroup>>()
    val groups: LiveData<List<TemplateGroup>> = _groups

    private val _templates = MutableLiveData<List<Template>>()
    val templates: LiveData<List<Template>> = _templates
   // val templates: LiveData<List<Template>> = templateDao.getAll()

    init {
        loadDataFromDb()
    }

    private fun loadDataFromDb(){
        CoroutineScope(Dispatchers.Main).launch {
            val groups = async(Dispatchers.IO) { loadGroups() }
            val templates = async(Dispatchers.IO) { loadTemplates() }
            _groups.value = groups.await()
          //  _templates.value = templates.await()
        }

    }

    private fun loadGroups(): List<TemplateGroup>?{
        val result = mutableListOf<TemplateGroup>()
        result.add(TemplateGroup(name = "Квартира на оболони", description =  "кухня"))
        result.add(TemplateGroup(name = "Гараж", description =  "отопление"))
        result.add(TemplateGroup(name = "Работа", description =  "кофе машина"))
        result.add(TemplateGroup(name = "Дача", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача2", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача4", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача5", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача6", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача7", description =  "система полива"))
        return result
       // return database.templateGroupDao().getAll()
    }

    private fun loadTemplates() {
      //  database.templateDao().getAll()
    }


    fun addGroup(item: TemplateGroup){
        val index = getGroupIndex(item.id)
        if (index != -1) _groups.updateItem(index, item)
        else _groups.addItem(item)
    }

    fun updateGroup(item: TemplateGroup){
        _groups.updateItem(getGroupIndex(item.id), item)
    }

    fun removeGroup(id: String){
        getGroupById(id)?.let { _groups.removeItem(it) }
    }

    suspend fun addTemplate(item: Template){
        val index = getTemplateIndex(item.id)
        if (index != -1) _templates.updateItem(index, item)
        else {
            _templates.addItem(item)
            increaseGroupSize(item.groupId)
        }
       /* withContext(Dispatchers.IO){
            database.templateDao().insert(item)
        }*/
    }

    fun updateTemplate(item: Template){
        _templates.updateItem(getTemplateIndex(item.id), item)
    }

    fun removeTemplate(id: String){
        getTemplateById(id)?.let {
            _templates.removeItem(it)
            decreaseGroupSize(it.groupId)
        }
    }

    private fun getTemplateIndex(id: String): Int {
        return templates.value?.let { list -> list.indexOfFirst { it.id == id } } ?: -1
    }

    private fun getGroupIndex(id: String): Int {
        return groups.value?.let { list -> list.indexOfFirst { it.id == id } } ?: -1
    }

    fun removeTemplate(item: Template){
        _templates.removeItem(item)
        decreaseGroupSize(item.groupId)
    }

    fun increaseGroupSize(id: String){
        getGroupById(id)?.run {
                size++
                updateGroups()
        }
    }

    fun decreaseGroupSize(id: String){
        getGroupById(id)?.run {
                size--
                updateGroups()
        }
    }

    fun updateGroups(){
        _groups.update()
    }

    fun getGroupById(id: String): TemplateGroup?{
        return groups.value?.find { it.id == id }
    }

    fun getTemplateById(id: String): Template?{
        return templates.value?.find { it.id == id }
    }
}