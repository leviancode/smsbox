package com.leviancode.android.gsmbox.data.repositories

import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientDao
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientGroupDao
import com.leviancode.android.gsmbox.data.entities.recipients.*
import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups
import com.leviancode.android.gsmbox.domain.repositories.RecipientsRepository
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

    override suspend fun getRecipients() = withContext(IO){
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

    override suspend fun getRecipientWithGroupsById(recipientId: Int) = withContext(IO)  {
        recipientDao.getRecipientWithGroupsById(recipientId)?.toDomainRecipientWithGroups()
    }

    override suspend fun save(item: Recipient): Int = withContext(IO){
        val recipientData = item.toRecipientData()
        recipientDao.upsert(recipientData)
    }

    override suspend fun save(item: RecipientWithGroups): Int {
        val recipientId = save(item.recipient)
        val recipientData = item.toRecipientWithGroupsData()
        relationDao.deleteByRecipientId(recipientId)
        recipientData.groups.forEach { group ->
            bind(group.recipientGroupId, recipientId)
        }
        return recipientId
    }


    override suspend fun save(list: List<Recipient>): IntArray = withContext(IO) {
        list.map { save(it) }.toIntArray()
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