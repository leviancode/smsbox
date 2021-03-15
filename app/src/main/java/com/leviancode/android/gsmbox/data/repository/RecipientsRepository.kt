package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object RecipientsRepository {
    private val recipientDao = AppDatabase.INSTANCE.recipientDao()
    private val groupDao = AppDatabase.INSTANCE.recipientGroupDao()
    var groups: LiveData<List<RecipientGroup>> = groupDao.getAllLiveData()
    var recipients: LiveData<List<Recipient>> = recipientDao.getAllLiveData()
    var groupedRecipients = groupDao.getGroupsWithRecipients().map { list ->
        list.onEach { it.group.size = it.recipients.size }
    }

    suspend fun saveRecipient(item: Recipient) = withContext(IO) {
        if (!item.groupName.isNullOrEmpty()){
            val group = getGroupByName(item.groupName!!)
            if (group == null) saveGroup(RecipientGroup(groupName = item.groupName!!))
        }

        val recipient = getRecipientById(item.recipientId)
        if (recipient == null) recipientDao.insert(item)
        else recipientDao.update(item)
    }

    suspend fun deleteRecipient(item: Recipient) = withContext(IO) {
        recipientDao.delete(item)
    }

    suspend fun saveGroup(item: RecipientGroup) = withContext(IO) {
        val group = getGroupById(item.groupId)
        if (group == null) groupDao.insert(item)
        else groupDao.update(item)
    }

    suspend fun deleteGroup(item: RecipientGroup) = withContext(IO) {
        recipientDao.deleteGroupFromAll(item.groupName)
        groupDao.delete(item)
    }

    suspend fun deleteGroupWithAllRecipients(item: RecipientGroup) = withContext(IO) {
        recipientDao.deleteByGroupName(item.groupName)
        groupDao.delete(item)
    }

    suspend fun getRecipientById(id: String) = withContext(IO) {
        recipientDao.get(id)
    }

    suspend fun getGroupById(id: String) = withContext(IO) {
        groupDao.getById(id)
    }

    suspend fun getGroupByName(name: String) = withContext(IO) {
        groupDao.getByName(name)
    }

    suspend fun removeGroupFromAllRecipients(item: RecipientGroup) = withContext(IO) {
        recipientDao.deleteGroupFromAll(item.groupName)
    }

    suspend fun getRecipientsByGroupName(groupName: String) = withContext(IO) {
        recipientDao.getByGroupName(groupName)
    }

    suspend fun updateAllRecipients(list: List<Recipient>) = withContext(IO) {
        recipientDao.insert(*list.toTypedArray())
    }
}