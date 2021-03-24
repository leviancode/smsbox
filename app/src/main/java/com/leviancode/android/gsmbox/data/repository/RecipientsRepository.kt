package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.RecipientDao
import com.leviancode.android.gsmbox.data.dao.RecipientGroupDao
import com.leviancode.android.gsmbox.data.dao.RecipientsAndGroupsCrossRefDao
import com.leviancode.android.gsmbox.data.model.recipients.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object RecipientsRepository {
    private val recipientDao: RecipientDao
        get() = AppDatabase.INSTANCE.recipientDao()
    private val groupDao: RecipientGroupDao
        get() = AppDatabase.INSTANCE.recipientGroupDao()
    private val recipientsAndGroupsDao: RecipientsAndGroupsCrossRefDao
        get() = AppDatabase.INSTANCE.recipientsAndGroupsDao()

    val groups: LiveData<List<RecipientGroup>>
        get() = groupDao.getAllLiveData()
    val recipients: LiveData<List<Recipient>>
        get() = recipientDao.getAllLiveData()

    val groupsWithRecipients: LiveData<List<GroupWithRecipients>>
        get() = recipientsAndGroupsDao.getGroupsWithRecipients()
    val recipientsWithGroups: LiveData<List<RecipientWithGroups>>
        get() = recipientsAndGroupsDao.getRecipientsWithGroups()

    suspend fun saveRecipient(item: Recipient) = withContext(IO) {
        val recipient = getRecipientById(item.recipientId)
        if (recipient == null) recipientDao.insert(item)
        else recipientDao.update(item)
    }

    suspend fun saveRecipientWithGroups(item: RecipientWithGroups) = withContext(IO) {
        saveRecipient(item.recipient)
        deleteRecipientFromAllGroups(item.recipient)
        item.groups.forEach { group ->
            saveGroupAndRecipientCrossRef(group.recipientGroupId, item.recipient.recipientId)
        }
    }

    suspend fun saveGroupWithRecipients(item: GroupWithRecipients) {
        deleteGroupFromAllRecipients(item.group)
        item.recipients.forEach { recipient ->
            saveGroupAndRecipientCrossRef(item.group.recipientGroupId, recipient.recipientId)
        }
    }

    suspend fun deleteRecipient(item: Recipient) = withContext(IO) {
        recipientsAndGroupsDao.deleteByRecipientId(item.recipientId)
        recipientDao.delete(item)
    }

    suspend fun deleteRecipientFromAllGroups(item: Recipient) = withContext(IO){
        recipientsAndGroupsDao.deleteByRecipientId(item.recipientId)
    }

    suspend fun deleteGroupFromAllRecipients(item: RecipientGroup) = withContext(IO){
        recipientsAndGroupsDao.deleteByGroupId(item.recipientGroupId)
    }

    suspend fun saveGroup(item: RecipientGroup) = withContext(IO) {
        val group = getGroupById(item.recipientGroupId)
        if (group == null) groupDao.insert(item)
        else groupDao.update(item)
    }

    suspend fun deleteGroup(item: RecipientGroup) = withContext(IO) {
        recipientsAndGroupsDao.deleteByGroupId(item.recipientGroupId)
        groupDao.delete(item)
    }

    suspend fun deleteGroupAndRecipientCrossRef(groupId: String, recipientId: String){
        recipientsAndGroupsDao.delete(
            RecipientsAndGroupsCrossRef(groupId, recipientId)
        )
    }

    suspend fun saveGroupAndRecipientCrossRef(groupId: String, recipientId: String){
        recipientsAndGroupsDao.insert(
            RecipientsAndGroupsCrossRef(groupId, recipientId)
        )
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

    suspend fun clearGroup(item: RecipientGroup) = withContext(IO) {
        recipientsAndGroupsDao.deleteByGroupId(item.recipientGroupId)
    }

    suspend fun getGroupWithRecipients(groupId: String) = withContext(IO) {
        recipientsAndGroupsDao.getGroupWithRecipients(groupId)
    }

    suspend fun getRecipientWithGroups(recipientId: String) = withContext(IO) {
        recipientsAndGroupsDao.getRecipientWithGroups(recipientId)
    }

    suspend fun updateAllRecipients(list: List<Recipient>) = withContext(IO) {
        recipientDao.insert(*list.toTypedArray())
    }

    fun getEmptyRecipientWithGroups() = RecipientWithGroups(Recipient(), mutableListOf())

    fun getNewRecipient() = Recipient()
    fun getNewRecipientGroup() = RecipientGroup()
    fun contactToRecipient(contact: Contact): Recipient {
        return Recipient(recipientName = contact.name, phoneNumber = contact.phoneNumber)
    }
}