package com.leviancode.android.gsmbox.domain.repositories

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder
import kotlinx.coroutines.flow.Flow

interface PlaceholdersRepository {
    fun getPlaceholdersObservable(): Flow<List<Placeholder>>

    suspend fun getById(id: Int): Placeholder?

    suspend fun getValue(name: String): String?

    suspend fun getByName(name: String): Placeholder?

    suspend fun delete(item: Placeholder)

    suspend fun deleteById(id: Int)

    suspend fun save(item: Placeholder)

}