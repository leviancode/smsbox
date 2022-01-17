package com.brainymobile.android.smsbox.data.repositories

import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientGroupDao
import com.brainymobile.android.smsbox.data.entities.recipients.*
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import com.brainymobile.android.smsbox.domain.repositories.RecipientGroupsRepository
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
       return groupDao.getGroupsWithRecipientsObservable().map { it.toDomainRecipientGroupsWithRecipients() }
    }

    override suspend fun getAll(): List<RecipientGroup> = withContext(IO)  {
        groupDao.getGroupsWithRecipients().map { it.toDomainRecipientGroup() }
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

    override suspend fun update(item: RecipientGroup): Int = withContext(IO)  {
        save(item)
    }

    override suspend fun update(items: List<RecipientGroup>) {
        items.forEach { update(it) }
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