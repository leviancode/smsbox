package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.utils.dependantLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object RecipientsRepository {
    private val recipientDao = AppDatabase.INSTANCE!!.recipientDao()
    private val groupDao = AppDatabase.INSTANCE!!.recipientGroupDao()
    var groups: LiveData<List<RecipientGroup>> = groupDao.getAll()
    var recipients: LiveData<List<Recipient>> = recipientDao.getAll()

    var groupedRecipients = dependantLiveData(recipients, groups, defaultValue = listOf()){
        groups.value?.onEach { group ->
            recipients.value?.filter { group.groupId == it.groupId }
                .orEmpty()
                .also { group.recipients.addAll(it) }
        }.orEmpty()
    }

    suspend fun saveRecipient(item: Recipient) = withContext(IO){
        val recipient = getRecipientById(item.recipientId)
        if (recipient == null) {
            if (item.groupName.isNotBlank() && item.groupId.isBlank()){
                val group = RecipientGroup(name = item.groupName)
                item.groupId = group.groupId
                saveGroup(group)
            }
            recipientDao.insert(item)
        }
        else recipientDao.update(item)
    }

    suspend fun deleteRecipient(item: Recipient) = withContext(IO){
        recipientDao.delete(item)
    }

    suspend fun saveGroup(item: RecipientGroup)  = withContext(IO){
        val group = getGroupById(item.groupId)
        if (group == null) groupDao.insert(item)
        else groupDao.update(item)
    }

    suspend fun deleteGroup(item: RecipientGroup) = withContext(IO){
        groupDao.delete(item)
    }

    suspend fun getRecipientById(id: String) = withContext(IO){
        recipientDao.get(id)
    }

    suspend fun getGroupById(id: String) = withContext(IO){
        groupDao.get(id)
    }
}