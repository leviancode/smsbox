package com.leviancode.android.gsmbox.domain.usecases.recipients.groups.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.repositories.RecipientGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.SaveRecipientGroupsUseCase

class SaveRecipientGroupsUseCaseImpl(private val repository: RecipientGroupsRepository) :
    SaveRecipientGroupsUseCase {
    override suspend fun save(item: RecipientGroup): Int {
        return repository.save(item)
    }

    override suspend fun save(items: List<RecipientGroup>): IntArray {
        return repository.save(items)
    }
}