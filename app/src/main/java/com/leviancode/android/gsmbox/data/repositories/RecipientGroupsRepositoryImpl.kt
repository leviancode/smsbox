package com.leviancode.android.gsmbox.data.repositories

import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientDao
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientGroupDao
import com.leviancode.android.gsmbox.data.entities.recipients.*
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.repositories.RecipientGroupsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecipientGroupsRepositoryImpl(
    private val groupDao: RecipientGroupDao,
    private val recipientDao: RecipientDao,
    private val relationDao: RecipientAndGroupRelationDao
): RecipientGroupsRepository{
    override fun getAllObservable(): Flow<List<RecipientGroup>> {
       return groupDao.getGroupsWithRecipients().map { it.toDomainRecipientGroupsWithRecipients() }
    }

    override suspend fun getById(groupId: Int) = withContext(IO) {
        groupDao.getGroupWithRecipients(groupId)?.toDomainRecipientGroup()
    }

    override suspend fun getByIds(ids: List<Int>) = withContext(IO) {
       ids.mapNotNull { getById(it) }
    }

    override suspend fun getByName(name: String) = withContext(IO) {
        groupDao.getGroupsWithRecipientsByName(name)?.toDomainRecipientGroup()
    }

    override suspend fun save(item: RecipientGroup) = withContext(IO) {
        val groupWithRecipients = item.toDataGroupWithRecipients()

        val groupId = groupDao.upsert(groupWithRecipients.group)
        deleteRecipientsFromGroup(groupId)
        groupWithRecipients.recipients.forEach { recipient ->
            val recipientId = recipientDao.upsert(recipient)
            bind(groupId, recipientId)
        }
        recipientDao.deleteUnused()
        groupId
    }

    override suspend fun save(items: List<RecipientGroup>): IntArray = withContext(IO) {
        items.map { save(it) }.toIntArray()
    }

    private suspend fun bind(groupId: Int, recipientId: Int) = withContext(IO) {
        relationDao.insert(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    override suspend fun delete(item: RecipientGroup) = withContext(IO) {
        groupDao.delete(item.toRecipientGroupData())
        recipientDao.deleteUnused()
    }

    override suspend fun deleteRecipientsFromGroup(groupId: Int) = withContext(IO) {
        relationDao.deleteByGroupId(groupId)
    }

    override suspend fun delete(id: Int) = withContext(IO) {
        groupDao.deleteById(id)
        recipientDao.deleteUnused()
    }

    override suspend fun unbind(groupId: Int, recipientId: Int) {
        relationDao.delete(
            RecipientsAndGroupRelation(
                recipientGroupId = groupId,
                recipientId = recipientId
            )
        )
    }
}