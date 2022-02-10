package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups
import com.brainymobile.android.smsbox.domain.repositories.RecipientsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.DeleteRecipientsUseCase
import javax.inject.Inject

class DeleteRecipientsUseCaseImpl @Inject constructor(private val repository: RecipientsRepository) :
    DeleteRecipientsUseCase {
    override suspend fun delete(item: Recipient) {
        repository.delete(item)
    }

    override suspend fun delete(item: RecipientWithGroups) {
        repository.delete(item)
    }
}