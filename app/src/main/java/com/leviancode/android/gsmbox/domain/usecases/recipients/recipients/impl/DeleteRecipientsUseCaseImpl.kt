package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups
import com.leviancode.android.gsmbox.domain.repositories.RecipientsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.DeleteRecipientsUseCase

class DeleteRecipientsUseCaseImpl(private val repository: RecipientsRepository) :
    DeleteRecipientsUseCase {
    override suspend fun delete(item: Recipient) {
        repository.delete(item)
    }

    override suspend fun delete(item: RecipientWithGroups) {
        repository.delete(item)
    }
}