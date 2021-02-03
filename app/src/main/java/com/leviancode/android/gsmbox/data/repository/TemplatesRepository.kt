package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.utils.addItem
import com.leviancode.android.gsmbox.utils.removeItem
import com.leviancode.android.gsmbox.utils.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object TemplatesRepository {
    private val _groups = MutableLiveData<List<TemplateGroup>>()
    val groups: LiveData<List<TemplateGroup>> = _groups

    private val _templates = MutableLiveData<List<Template>>()
    val templates: LiveData<List<Template>> = _templates

    init {
        loadDataFromDb()
    }

    private fun loadDataFromDb(){
        CoroutineScope(Dispatchers.Main).launch {
            val groups = async(Dispatchers.IO) { loadGroups() }
            val templates = async(Dispatchers.IO) { loadTemplates() }
            _groups.value = groups.await()
            _templates.value = templates.await()
        }

    }

    private fun loadGroups(): List<TemplateGroup>{
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
    }

    private fun loadTemplates(): List<Template>{
        val result = mutableListOf<Template>()

        return result
    }


    fun addGroup(item: TemplateGroup){
        _groups.addItem(item)
    }

    fun removeGroup(group: TemplateGroup){
        _groups.removeItem(group)
    }

    fun addTemplate(item: Template){
        _templates.addItem(item)
        increaseGroupSize(item.groupId)
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

    fun updateGroup(item: TemplateGroup){
        groups.value?.toMutableList()?.let{ list ->
            val index = list.indexOfFirst { item.id == it.id }
            if (index == -1){
                list.add(item)
            } else {
                list[index] = item
            }
            _groups.value = list
        }

    }

    fun updateTemplate(item: Template){
        templates.value?.toMutableList()?.let{ list ->
            val index = list.indexOfFirst { item.id == it.id }
            if (index == -1){
                list.add(item)
            } else {
                list[index] = item
            }
            _templates.value = list
        }
    }

    fun getGroupById(id: String): TemplateGroup?{
        return groups.value?.find { it.id == id }
    }

    fun getTemplateById(id: String): Template?{
        return templates.value?.find { it.id == id }
    }
}