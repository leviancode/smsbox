package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.recipients.RecipientAndGroupRelationDao
import com.leviancode.android.gsmbox.data.dao.recipients.RecipientDao
import com.leviancode.android.gsmbox.data.dao.recipients.RecipientGroupDao
import com.leviancode.android.gsmbox.data.model.recipients.*
import com.leviancode.android.gsmbox.utils.extensions.toIntArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

object RecipientsRepository {
    private  val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val recipientDao: RecipientDao
        get() = AppDatabase.instance.recipientDao()
    private val groupDao: RecipientGroupDao
        get() = AppDatabase.instance.recipientGroupDao()
    private val relationDao: RecipientAndGroupRelationDao
        get() = AppDatabase.instance.recipientAndGroupRelationDao()

    fun getGroups(): LiveData<List<RecipientGroup>> = groupDao.getAllLiveData()

    fun getGroupsWithRecipients(): LiveData<List<GroupWithRecipients>> = groupDao.getGroupsWithRecipients()

    fun getRecipientsWithGroups(): LiveData<List<RecipientWithGroups>> = recipientDao.getRecipientsWithGroups()

    fun getRecipients(): LiveData<List<Recipient>> = recipientDao.getAllLiveData()

    suspend fun saveRecipient(item: Recipient): Int {
        return withContext(scope.coroutineContext) {
            recipientDao.upsert(item)
        }
    }

    suspend fun saveGroup(item: RecipientGroup): Int {
        return withContext(scope.coroutineContext) {
            groupDao.upsert(item)
        }
    }

    suspend fun insertAllRecipients(list: List<Recipient>): IntArray {
        return withContext(scope.coroutineContext) {
            recipientDao.insertAll(list).toIntArray()
        }
    }

    suspend fun insertAllGroups(list: List<RecipientGroup>): IntArray {
        return withContext(scope.coroutineContext) {
            groupDao.insertAll(list).toIntArray()
        }
    }

    suspend fun saveRecipientWithGroups(item: RecipientWithGroups): Int {
        return withContext(scope.coroutineContext) {
            val recipientId = recipientDao.upsert(item.recipient)
            relationDao.deleteByRecipientId(recipientId)
            item.groups.forEach { group ->
                val groupId = saveGroup(group)
                saveRelation(groupId, recipientId)
            }
            recipientId
        }
    }

    suspend fun saveRecipientWithGroups(item: Recipient, groupIds: List<Int>): Int {
        return withContext(scope.coroutineContext) {
            val recipientId = recipientDao.upsert(item)
            relationDao.deleteByRecipientId(recipientId)
            groupIds.toList().forEach { groupId ->
                saveRelation(groupId, recipientId)
            }
            recipientId
        }
    }

    suspend fun saveGroupWithRecipients(item: GroupWithRecipients): Int {
        return withContext(scope.coroutineContext) {
            val groupId = groupDao.upsert(item.group)
            deleteAllRelationWithGroup(groupId)
            item.getRecipients().forEach { recipient ->
                val recipientId = saveRecipient(recipient)
                saveRelation(groupId, recipientId)
            }
            recipientDao.deleteUnused()
            groupId
        }

    }

    suspend fun deleteRecipient(item: Recipient) = withContext(scope.coroutineContext) {
        val groupIds = relationDao.getGroupsWithRecipientRelation(item.recipientId)
        if (groupIds.isNotEmpty()){
            var inNoNameGroup = false
            groupIds.forEach { groupId ->
                groupDao.getById(groupId)?.let { group ->
                    if (group.getName().isNullOrBlank()){
                        inNoNameGroup = true
                    } else {
                        deleteRelation(groupId, item.recipientId)
                    }
                }
            }
            if (inNoNameGroup){
                item.setName(null)
                recipientDao.update(item)
            } else {
                recipientDao.delete(item)
            }
        } else {
            recipientDao.delete(item)
        }
    }

    suspend fun deleteGroup(item: RecipientGroup) = withContext(scope.coroutineContext) {
        groupDao.delete(item)
    }

    suspend fun deleteRelation(groupId: Int, recipientId: Int) = withContext(scope.coroutineContext) {
        relationDao.delete(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    suspend fun saveRelation(groupId: Int, recipientId: Int) = withContext(scope.coroutineContext) {
        relationDao.insert(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    suspend fun getGroupById(groupId: Int) = withContext(scope.coroutineContext) {
        groupDao.getById(groupId)
    }

    suspend fun getGroupByIds(ids: List<Int>) = withContext(scope.coroutineContext) {
        groupDao.getByIds(*ids.toIntArray())
    }

    suspend fun deleteAllRelationWithGroup(groupId: Int) = withContext(scope.coroutineContext) {
        relationDao.deleteByGroupId(groupId)
    }

    suspend fun getGroupWithRecipients(groupId: Int) = withContext(scope.coroutineContext) {
        groupDao.getGroupWithRecipients(groupId)
    }

    suspend fun getRecipientWithGroups(recipientId: Int) = withContext(scope.coroutineContext) {
        recipientDao.getRecipientWithGroups(recipientId)
    }

    fun getNewRecipientWithGroups() = RecipientWithGroups(Recipient(), mutableListOf())

    fun getNewGroupWithRecipients() = GroupWithRecipients()

    fun getNewRecipient() = Recipient()

    fun getNewRecipientGroup() = RecipientGroup()

    fun getRecipientNumbersLiveData() = recipientDao.getNumbersWithNotEmptyNamesLiveData()

    suspend fun getRecipientNumbers() = withContext(scope.coroutineContext) {
        recipientDao.getNumbersWithNotEmptyNames()
    }

    suspend fun getRecipientByName(name: String) = withContext(scope.coroutineContext) {
        recipientDao.getByName(name)
    }

    suspend fun getGroupByName(name: String) = withContext(scope.coroutineContext) {
        groupDao.getByName(name)
    }

    suspend fun isRelationExist(recipientGroupId: Int, recipientId: Int) = withContext(scope.coroutineContext) {
        relationDao.get(recipientGroupId, recipientId) != null
    }

    suspend fun deleteGroupById(id: Int) = withContext(scope.coroutineContext) {
        groupDao.deleteById(id)
        recipientDao.deleteUnused()
    }

    suspend fun getRecipientByPhoneNumber(value: String) = withContext(scope.coroutineContext) {
        recipientDao.getByPhoneNumber(value)
    }
}