package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import com.brainymobile.android.smsbox.domain.repositories.RecipientsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.UpdateRecipientsUseCase

class UpdateRecipientsUseCaseImpl(private val repository: RecipientsRepository) : UpdateRecipientsUseCase {

    override suspend fun update(items: List<Recipient>) {
        repository.update(items)
    }

    override suspend fun update(item: RecipientWithGroups): Int {
        return repository.update(item)
    }
}