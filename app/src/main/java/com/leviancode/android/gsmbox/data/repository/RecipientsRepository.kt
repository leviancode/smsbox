package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object RecipientsRepository {
    private val recipientDao = AppDatabase.INSTANCE!!.recipientDao()
    private val groupDao = AppDatabase.INSTANCE!!.recipientGroupDao()
    var groups: LiveData<List<RecipientGroup>> = groupDao.getAll()
    var recipients: LiveData<List<Recipient>> = recipientDao.getAll()
    var groupedRecipients = groupDao.getGroupsWithRecipients()

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
}