package com.brainymobile.android.smsbox.domain.usecases.placeholders

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder
import kotlinx.coroutines.flow.Flow

interface FetchPlaceholdersUseCase {
    fun getPlaceholders(): Flow<List<Placeholder>>
    suspend fun getValue(name: String): String?
    suspend fun getByName(name: String): Placeholder?
    suspend fun getById(id: Int): Placeholder?
}