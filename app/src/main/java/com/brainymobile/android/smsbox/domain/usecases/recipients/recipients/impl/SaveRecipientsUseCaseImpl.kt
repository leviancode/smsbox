package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import com.brainymobile.android.smsbox.domain.repositories.RecipientsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.SaveRecipientsUseCase

class SaveRecipientsUseCaseImpl(private val repository: RecipientsRepository):
    SaveRecipientsUseCase {

    override suspend fun save(item: Recipient): Int {
        return  repository.save(updatePosition(item))
    }

    override suspend fun save(item: RecipientWithGroups): Int {
        return repository.save(item.apply {
            recipient = updatePosition(item.recipient)
        })
    }

    private suspend fun updatePosition(item: Recipient): Recipient {
        return item.apply {
            position = repository.count()
        }
    }
}