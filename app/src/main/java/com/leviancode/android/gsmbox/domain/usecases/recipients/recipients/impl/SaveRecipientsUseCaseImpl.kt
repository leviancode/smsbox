package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups
import com.leviancode.android.gsmbox.domain.repositories.RecipientsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.SaveRecipientsUseCase

class SaveRecipientsUseCaseImpl(private val repository: RecipientsRepository):
    SaveRecipientsUseCase {
    override suspend fun save(item: Recipient): Int {
        return  repository.save(item)
    }

    override suspend fun save(item: RecipientWithGroups): Int {
        return repository.save(item)
    }

    override suspend fun save(list: List<Recipient>): IntArray {
        return  repository.save(list)
    }
}