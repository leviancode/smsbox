package com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import com.brainymobile.android.smsbox.domain.repositories.RecipientGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.SaveRecipientGroupsUseCase
import javax.inject.Inject

class SaveRecipientGroupsUseCaseImpl @Inject constructor(private val repository: RecipientGroupsRepository) :
    SaveRecipientGroupsUseCase {
    override suspend fun save(item: RecipientGroup): Int {
        return repository.save(item)
    }

    override suspend fun save(items: List<RecipientGroup>): IntArray {
        return repository.save(items)
    }
}