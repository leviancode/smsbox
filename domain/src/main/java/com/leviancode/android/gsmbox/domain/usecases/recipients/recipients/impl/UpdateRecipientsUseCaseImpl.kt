package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups
import com.leviancode.android.gsmbox.domain.repositories.RecipientsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.UpdateRecipientsUseCase

class UpdateRecipientsUseCaseImpl(private val repository: RecipientsRepository) : UpdateRecipientsUseCase {

    override suspend fun update(items: List<Recipient>) {
        repository.update(items)
    }

    override suspend fun update(item: RecipientWithGroups): Int {
        return repository.update(item)
    }
}