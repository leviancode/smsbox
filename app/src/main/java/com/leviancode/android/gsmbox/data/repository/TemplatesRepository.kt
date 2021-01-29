package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
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
        /*result.add(TemplateGroup(name = "Квартира на оболони", description =  "кухня"))
        result.add(TemplateGroup(name = "Гараж", description =  "отопление"))
        result.add(TemplateGroup(name = "Работа", description =  "кофе машина"))
        result.add(TemplateGroup(name = "Дача", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача2", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
        result.add(TemplateGroup(name = "Дача3", description =  "система полива"))
*/
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
    }

    fun removeTemplate(item: Template){
        _templates.removeItem(item)
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
}

fun <T> MutableLiveData<List<T>>.addItem(item: T) {
    val items = this.value as ArrayList
    items.add(item)
    this.value = items
}

fun <T> MutableLiveData<List<T>>.removeItem(item: T) {
    val items = this.value as ArrayList
    items.remove(item)
    this.value = items
}