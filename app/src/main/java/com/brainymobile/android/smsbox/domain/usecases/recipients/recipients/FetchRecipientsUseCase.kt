package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import kotlinx.coroutines.flow.Flow

interface FetchRecipientsUseCase {

    suspend fun getRecipients(): List<Recipient>

    suspend fun getRecipientsWithGroups(): List<RecipientWithGroups>

    fun getRecipientsObservable(): Flow<List<Recipient>>

    suspend fun getById(recipientId: Int): Recipient?

    suspend fun getRecipientWithGroupsById(recipientId: Int): RecipientWithGroups?

    suspend fun getByName(name: String): Recipient?

    suspend fun getByPhoneNumber(number: String): Recipient?

    fun getPhoneNumbersObservable(): Flow<List<String>>

    suspend fun getPhoneNumbers(): List<String>
}