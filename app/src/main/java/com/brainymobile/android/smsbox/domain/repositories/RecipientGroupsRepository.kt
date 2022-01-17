package com.brainymobile.android.smsbox.domain.repositories

import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import kotlinx.coroutines.flow.Flow

interface RecipientGroupsRepository {
    fun getAllObservable(): Flow<List<RecipientGroup>>

    suspend fun getById(groupId: Int): RecipientGroup?

    suspend fun getByIds(ids: List<Int>): List<RecipientGroup>

    suspend fun getByName(name: String): RecipientGroup?

    suspend fun save(item: RecipientGroup): Int

    suspend fun update(item: RecipientGroup): Int

    suspend fun update(items: List<RecipientGroup>)

    suspend fun save(items: List<RecipientGroup>): IntArray

    suspend fun delete(item: RecipientGroup)

    suspend fun delete(id: Int)

    suspend fun deleteRecipientsFromGroup(groupId: Int)

    suspend fun unbind(groupId: Int, recipientId: Int)

    suspend fun getAll(): List<RecipientGroup>
}