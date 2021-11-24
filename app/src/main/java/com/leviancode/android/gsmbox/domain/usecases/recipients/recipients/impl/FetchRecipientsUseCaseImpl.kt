package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups
import com.leviancode.android.gsmbox.domain.repositories.RecipientsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
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

    override fun createRecipient() = Recipient(
        id = 0,
        name = "",
        phoneNumber = ""
    )

    override fun getPhoneNumbersObservable() = repository.getPhoneNumbersObservable()

    override suspend fun getPhoneNumbers() = repository.getPhoneNumbers()

    override suspend fun getByName(name: String) =  repository.getRecipientByName(name)

    override suspend fun getByPhoneNumber(number: String) = withContext(Dispatchers.IO) {
        repository.getRecipientByPhoneNumber(number)
    }

}