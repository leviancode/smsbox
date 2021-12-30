package com.leviancode.android.gsmbox.domain.usecases.recipients.groups

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import kotlinx.coroutines.flow.Flow

interface FetchRecipientGroupsUseCase {
    fun getAllObservable(): Flow<List<RecipientGroup>>

    suspend fun getAll(): List<RecipientGroup>

    suspend fun getById(groupId: Int): RecipientGroup?

    suspend fun getByIds(ids: List<Int>): List<RecipientGroup>

    suspend fun getByName(name: String): RecipientGroup?
}