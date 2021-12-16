package com.leviancode.android.gsmbox.domain.usecases.recipients.groups.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.repositories.RecipientGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import kotlinx.coroutines.flow.Flow

class FetchRecipientGroupsUseCaseImpl(private val repository: RecipientGroupsRepository) :
    FetchRecipientGroupsUseCase {
    override fun getAll(): Flow<List<RecipientGroup>> {
       return repository.getAllObservable()
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