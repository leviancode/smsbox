package com.brainymobile.android.smsbox.data.repositories

import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientGroupDao
import com.brainymobile.android.smsbox.data.entities.recipients.*
import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import com.brainymobile.android.smsbox.domain.repositories.RecipientsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecipientsRepositoryImpl(
    private val recipientDao: RecipientDao,
    private val groupDao: RecipientGroupDao,
    private val relationDao: RecipientAndGroupRelationDao
) : RecipientsRepository {

    override fun getRecipientsObservable(): Flow<List<Recipient>> {
        return recipientDao.getAllObservable().map { it.toDomainRecipients() }
    }

    override suspend fun getRecipients() = withContext(IO) {
        recipientDao.getAll().toDomainRecipients()
    }

    override suspend fun getRecipientsWithGroups() = withContext(IO) {
        recipientDao.getRecipientsWithGroups().toDomainRecipientsWithGroups()
    }

    override suspend fun getById(recipientId: Int) = withContext(IO) {
        recipientDao.getRecipientWithGroupsById(recipientId)?.toDomainRecipient()
    }

    override fun getPhoneNumbersObservable() = recipientDao.getNumbersWithNotEmptyNamesObservable()

    override suspend fun getPhoneNumbers() = withContext(IO) {
        recipientDao.getNumbersWithNotEmptyNames()
    }

    override suspend fun getRecipientByName(name: String) = withContext(IO) {
        recipientDao.getRecipientWithGroupsByName(name)?.toDomainRecipient()
    }

    override suspend fun getRecipientByPhoneNumber(phoneNumber: String) = withContext(IO) {
        recipientDao.getRecipientWithGroupsByPhoneNumber(phoneNumber)?.toDomainRecipient()
    }

    override suspend fun getRecipientWithGroupsById(recipientId: Int) = withContext(IO) {
        recipientDao.getRecipientWithGroupsById(recipientId)?.toDomainRecipientWithGroups()
    }

    override suspend fun save(item: Recipient): Int = withContext(IO) {
        val recipientData = item.toRecipientData()
        recipientDao.upsert(recipientData)
    }

    override suspend fun save(item: RecipientWithGroups): Int = withContext(IO) {
        val recipientId = save(item.recipient)
        val recipientData = item.toRecipientWithGroupsData().apply {
            groups = searchAndAddNullNameGroups(recipientId)
        }
        relationDao.deleteByRecipientId(recipientId)
        recipientData.groups.forEach { group ->
            bind(group.recipientGroupId, recipientId)
        }
        recipientId
    }

    private suspend fun RecipientWithGroupsData.searchAndAddNullNameGroups(
        recipientId: Int
    ): List<RecipientGroupData> = groups.toMutableList().also { groups ->
        val groupsIds = relationDao.getGroupsWithRecipientRelation(recipientId)
        groupsIds.forEach { id ->
            groupDao.getById(id)?.let { group ->
                if (group.name == null) groups.add(group)
            }
        }
    }

    override suspend fun delete(item: Recipient) = withContext(IO) {
        val recipientData = item.toRecipientData()
        recipientDao.delete(recipientData)
        relationDao.deleteByRecipientId(recipientData.recipientId)
    }

    override suspend fun delete(item: RecipientWithGroups) = withContext(IO) {
        val recipientData = item.toRecipientWithGroupsData()
        if (recipientData.groups.isNotEmpty()) {
            recipientData.groups.forEach { group ->
                unbind(group.recipientGroupId, recipientData.recipient.recipientId)
            }
            groupDao.deleteUnused()
        }
        recipientDao.delete(recipientData.recipient)
    }

    override suspend fun update(items: List<Recipient>) = withContext(IO) {
        recipientDao.update(*items.toRecipientsData().toTypedArray())
    }

    override suspend fun update(item: RecipientWithGroups) = withContext(IO) {
        val recipientData = item.toRecipientWithGroupsData().apply {
            groups = searchAndAddNullNameGroups(item.recipient.id)
        }
        recipientDao.update(recipientData.recipient)
        val recipientId = recipientData.recipient.recipientId
        relationDao.deleteByRecipientId(recipientId)
        recipientData.groups.forEach { group ->
            bind(group.recipientGroupId, recipientId)
        }
        recipientId
    }

    override suspend fun count(): Int = withContext(IO) { recipientDao.count() }

    private suspend fun unbind(groupId: Int, recipientId: Int) = withContext(IO) {
        relationDao.delete(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    private suspend fun bind(groupId: Int, recipientId: Int) = withContext(IO) {
        relationDao.insert(
            RecipientsAndGroupRelation(groupId, recipientId)
        )
    }

    override suspend fun isBind(recipientGroupId: Int, recipientId: Int) = withContext(IO) {
        relationDao.get(recipientGroupId, recipientId) != null
    }
}