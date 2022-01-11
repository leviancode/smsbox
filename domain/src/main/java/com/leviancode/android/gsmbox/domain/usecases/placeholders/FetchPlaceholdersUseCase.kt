package com.leviancode.android.gsmbox.domain.usecases.placeholders

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder
import kotlinx.coroutines.flow.Flow

interface FetchPlaceholdersUseCase {
    fun getPlaceholders(): Flow<List<Placeholder>>
    suspend fun getValue(name: String): String?
    suspend fun getByName(name: String): Placeholder?
    suspend fun getById(id: Int): Placeholder?
}