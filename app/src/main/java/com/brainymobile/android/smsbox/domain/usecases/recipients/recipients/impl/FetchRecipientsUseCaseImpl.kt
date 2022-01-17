package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import com.brainymobile.android.smsbox.domain.repositories.RecipientsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchRecipientsUseCaseImpl(private val repository: RecipientsRepository):
    FetchRecipientsUseCase {

    override suspend fun getRecipients() = repository.getRecipients()

    override suspend fun getRecipientsWithGroups(): List<RecipientWithGroups> {
        return repository.getRecipientsWithGroups()
    }

    override fun getRecipientsObservable() = repository.getRecipientsObservable()

    override suspend fun getById(recipientId: Int) = withContext(Dispatchers.IO) {
        repository.getById(recipientId)
    }

    override suspend fun getRecipientWithGroupsById(recipientId: Int): RecipientWithGroups? {
        return repository.getRecipientWithGroupsById(recipientId)
    }

    override fun getPhoneNumbersObservable() = repository.getPhoneNumbersObservable()

    override suspend fun getPhoneNumbers() = repository.getPhoneNumbers()

    override suspend fun getByName(name: String) =  repository.getRecipientByName(name)

    override suspend fun getByPhoneNumber(number: String) = withContext(Dispatchers.IO) {
        repository.getRecipientByPhoneNumber(number)
    }

}