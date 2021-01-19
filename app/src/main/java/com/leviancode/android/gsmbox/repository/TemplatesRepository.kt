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
        result.add(TemplateGroup("Квартира на оболони", "кухонные девайсы"))
        groups = result

        withContext(Dispatchers.Main){
            notifyObservers()
        }
    }

    fun addGroup(item: TemplateGroup){
        groups.add(item)
        notifyObservers()
    }

    fun updateGroup(item: TemplateGroup){
        val index = groups.indexOfFirst { item.id == it.id }
        groups[index] = item
        notifyObservers()
    }

    fun removeGroup(group: TemplateGroup){
        groups.remove(group)
        notifyObservers()
    }

    fun clear(){
        groups.clear()
    }

    fun notifyObservers(){
        _groupsLiveData.value = groups
    }
}