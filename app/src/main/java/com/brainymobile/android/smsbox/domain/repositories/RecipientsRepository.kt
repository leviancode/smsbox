package com.brainymobile.android.smsbox.domain.repositories

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import kotlinx.coroutines.flow.Flow


interface RecipientsRepository {

    fun getRecipientsObservable(): Flow<List<Recipient>>

    suspend fun getRecipients(): List<Recipient>

    suspend fun getRecipientsWithGroups(): List<RecipientWithGroups>

    suspend fun getById(recipientId: Int): Recipient?

    fun getPhoneNumbersObservable(): Flow<List<String>>

    suspend fun getPhoneNumbers(): List<String>

    suspend fun getRecipientByName(name: String): Recipient?

    suspend fun getRecipientByPhoneNumber(phoneNumber: String): Recipient?

    suspend fun getRecipientWithGroupsById(recipientId: Int): RecipientWithGroups?

    suspend fun isBind(recipientGroupId: Int, recipientId: Int): Boolean

    suspend fun save(item: Recipient): Int

    suspend fun save(item: RecipientWithGroups): Int

    suspend fun delete(item: Recipient)

    suspend fun delete(item: RecipientWithGroups)

    suspend fun update(items: List<Recipient>)

    suspend fun update(item: RecipientWithGroups): Int

    suspend fun count(): Int

}