package com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import com.brainymobile.android.smsbox.domain.repositories.RecipientGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchRecipientGroupsUseCaseImpl @Inject constructor(private val repository: RecipientGroupsRepository) :
    FetchRecipientGroupsUseCase {
    override fun getAllObservable(): Flow<List<RecipientGroup>> {
       return repository.getAllObservable()
    }

    override suspend fun getAll(): List<RecipientGroup> {
        return repository.getAll()
    }

    override suspend fun getById(groupId: Int): RecipientGroup? {
       return repository.getById(groupId)
    }

    override suspend fun getByIds(ids: List<Int>): List<RecipientGroup> {
        return repository.getByIds(ids)
    }

    override suspend fun getByName(name: String): RecipientGroup? {
        return repository.getByName(name)
    }
}