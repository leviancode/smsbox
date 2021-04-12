package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.RecipientDao
import com.leviancode.android.gsmbox.data.dao.RecipientGroupDao
import com.leviancode.android.gsmbox.data.dao.RecipientsAndGroupRelationDao
import com.leviancode.android.gsmbox.data.model.recipients.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object RecipientsRepository {
    private val recipientDao: RecipientDao
        get() = AppDatabase.instance.recipientDao()
    private val groupDao: RecipientGroupDao
        get() = AppDatabase.instance.recipientGroupDao()
    private val recipientsAndGroupsDao: RecipientsAndGroupRelationDao
        get() = AppDatabase.instance.recipientsAndGroupsDao()

    val groups: LiveData<List<RecipientGroup>>
        get() = groupDao.getAllLiveData()
    val recipients: LiveData<List<Recipient>>
        get() = recipientDao.getAllLiveData()

    val groupsWithRecipients: LiveData<List<GroupWithRecipients>>
        get() = recipientsAndGroupsDao.getGroupsWithRecipients()
    val recipientsWithGroups: LiveData<List<RecipientWithGroups>>
        get() = recipientsAndGroupsDao.getRecipientsWithGroups()

    suspend fun saveRecipient(item: Recipient) = withContext(IO) {
        if (item.recipientId == 0L) recipientDao.insert(item)
        else {
            recipientDao.update(item)
            item.recipientId
        }
    }
    suspend fun saveGroup(item: RecipientGroup) = withContext(IO) {
        if (item.recipientGroupId == 0L) groupDao.insert(item)
        else {
            groupDao.update(item)
            item.recipientGroupId
        }
    }


    suspend fun saveRecipientWithGroups(item: RecipientWithGroups) = withContext(IO) {
        saveRecipient(item.recipient)
        deleteRecipientFromAllGroups(item.recipient)
        item.groups.forEach { group ->
            saveGroup(group)
            saveGroupAndRecipientRelation(group.recipientGroupId, item.recipient.recipientId)
        }
    }

    suspend fun saveGroupWithRecipients(item: GroupWithRecipients) = withContext(IO) {
        deleteGroupFromAllRecipients(item.group.recipientGroupId)
        saveGroup(item.group)
        item.getRecipients().forEach { recipient ->
            saveRecipient(recipient)
            saveGroupAndRecipientRelation(item.group.recipientGroupId, recipient.recipientId)
        }
    }

    suspend fun deleteRecipient(item: Recipient) = withContext(IO) {
        deleteRecipientFromAllGroups(item)
        recipientDao.delete(item)
    }

    suspend fun deleteRecipientFromAllGroups(item: Recipient) = withContext(IO){
        recipientsAndGroupsDao.deleteByRecipientId(item.recipientId)
    }

    suspend fun deleteGroupFromAllRecipients(groupId: Long) = withContext(IO){
        recipientsAndGroupsDao.deleteByGroupId(groupId)
    }

    suspend fun deleteGroup(item: RecipientGroup) = withContext(IO) {
        deleteGroupFromAllRecipients(item.recipientGroupId)
        groupDao.delete(item)
    }

    suspend fun deleteGroupAndRecipientRelation(groupId: Long, recipientId: Long) = withContext(IO){
        recipientsAndGroupsDao.delete(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    suspend fun saveGroupAndRecipientRelation(groupId: Long, recipientId: Long) = withContext(IO){
        recipientsAndGroupsDao.insert(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    suspend fun getRecipientById(recipientId: Long) = withContext(IO) {
        recipientDao.get(recipientId)
    }

    suspend fun getGroupById(groupId: Long) = withContext(IO) {
        groupDao.getById(groupId)
    }

    suspend fun clearGroup(item: RecipientGroup) = withContext(IO) {
        recipientsAndGroupsDao.deleteByGroupId(item.recipientGroupId)
    }

    suspend fun getGroupWithRecipients(groupId: Long) = withContext(IO) {
        recipientsAndGroupsDao.getGroupWithRecipients(groupId)
    }

    suspend fun getRecipientWithGroups(recipientId: Long) = withContext(IO) {
        recipientsAndGroupsDao.getRecipientWithGroups(recipientId)
    }

    suspend fun updateAllRecipients(list: List<Recipient>) = withContext(IO) {
        list.forEach { recipientDao.insert(it) }
    }

    fun getNewRecipientWithGroups() = RecipientWithGroups(Recipient(), mutableListOf())

    fun getNewGroupWithRecipients() = GroupWithRecipients()

    fun getNewRecipient() = Recipient()

    fun getNewRecipientGroup() = RecipientGroup()

    fun contactToRecipient(contact: Contact): Recipient {
        return Recipient(name = contact.name, phoneNumber = contact.phoneNumber)
    }

    fun getRecipientNumbersLiveData(): LiveData<List<String>> = recipientDao.getNumbersWithNotEmptyNamesLiveData()
    suspend fun getRecipientNumbers() = recipientDao.getNumbersWithNotEmptyNames()

    suspend fun getRecipientByName(name: String): Recipient? {
        return recipientDao.getByName(name)
    }

    suspend fun getGroupByName(name: String): RecipientGroup? {
        return groupDao.getByName(name)
    }
}