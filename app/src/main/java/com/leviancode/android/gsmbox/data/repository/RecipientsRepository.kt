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
        item.setRecipientName(item.getRecipientName().trim())
        val recipient = getRecipientById(item.recipientId)
        if (recipient == null) recipientDao.insert(item)
        else recipientDao.update(item)
    }

    suspend fun saveGroup(item: RecipientGroup) = withContext(IO) {
        item.setRecipientGroupName(item.getRecipientGroupName().trim())
        val group = getGroupById(item.recipientGroupId)
        if (group == null) groupDao.insert(item)
        else groupDao.update(item)
    }

    suspend fun saveRecipientWithGroups(item: RecipientWithGroups) = withContext(IO) {
        saveRecipient(item.recipient)
        deleteRecipientFromAllGroups(item.recipient)
        item.groups.forEach { group ->
            saveGroupAndRecipientCrossRef(group.recipientGroupId, item.recipient.recipientId)
        }
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
    }

    suspend fun saveGroupWithRecipients(item: GroupWithRecipients) {
        deleteGroupFromAllRecipients(item.group)
        item.recipients.forEach { recipient ->
            saveGroupAndRecipientCrossRef(item.group.recipientGroupId, recipient.recipientId)
        }
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
    }

    suspend fun deleteRecipient(item: Recipient) = withContext(IO) {
        deleteRecipientFromAllGroups(item)
        recipientDao.delete(item)
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
    }

    suspend fun deleteRecipientFromAllGroups(item: Recipient) = withContext(IO){
        recipientsAndGroupsDao.deleteByRecipientId(item.recipientId)
    }

    suspend fun deleteGroupFromAllRecipients(item: RecipientGroup) = withContext(IO){
        recipientsAndGroupsDao.deleteByGroupId(item.recipientGroupId)
    }

    suspend fun deleteGroup(item: RecipientGroup) = withContext(IO) {
        deleteGroupFromAllRecipients(item)
        groupDao.delete(item)
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
    }

    suspend fun deleteGroupAndRecipientCrossRef(groupId: String, recipientId: String) = withContext(IO){
        recipientsAndGroupsDao.delete(
            RecipientsAndGroupsCrossRef(groupId, recipientId)
        )
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
    }

    suspend fun saveGroupAndRecipientCrossRef(groupId: String, recipientId: String) = withContext(IO){
        recipientsAndGroupsDao.insert(
            RecipientsAndGroupsCrossRef(groupId, recipientId)
        )
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
    }

    suspend fun getRecipientById(recipientId: String) = withContext(IO) {
        recipientDao.get(recipientId)
    }

    suspend fun getGroupById(groupId: String) = withContext(IO) {
        groupDao.getById(groupId)
    }

    suspend fun clearGroup(item: RecipientGroup) = withContext(IO) {
        recipientsAndGroupsDao.deleteByGroupId(item.recipientGroupId)
        TemplatesRepository.updateRecipientGroupsInAllTemplates()
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