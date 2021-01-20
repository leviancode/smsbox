package com.leviancode.android.gsmbox.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leviancode.android.gsmbox.model.TemplateGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object TemplatesRepository {
    private var groups = mutableListOf<TemplateGroup>()
    private val _groupsLiveData = MutableLiveData<MutableList<TemplateGroup>>()
    val groupsLiveData: LiveData<MutableList<TemplateGroup>> = _groupsLiveData

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadData()
        }
    }

    suspend fun loadData(){
        val result = mutableListOf<TemplateGroup>()
        result.add(TemplateGroup(name = "Квартира на оболони", description =  "кухонные девайсы"))
        withContext(Dispatchers.Main){
            updateDataAndNotifyObservers(result)
        }
    }

    fun addGroup(item: TemplateGroup){
        val newList = groups.toMutableList()
        newList.add(item)
        updateDataAndNotifyObservers(newList)
    }

    fun updateGroup(item: TemplateGroup){
        val newList = groups.toMutableList()
        val index = newList.indexOfFirst { item.id == it.id }
        newList[index] = item
        updateDataAndNotifyObservers(newList)
    }

    fun removeGroup(group: TemplateGroup){
        val newList = groups.toMutableList()
        newList.remove(group)
        updateDataAndNotifyObservers(newList)
    }

    fun clear(){
        updateDataAndNotifyObservers(mutableListOf())
    }

    private fun updateDataAndNotifyObservers(newList: MutableList<TemplateGroup>){
        groups = newList
        _groupsLiveData.value = groups
    }
}