package com.brainymobile.android.smsbox.domain.usecases.placeholders.impl

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder
import com.brainymobile.android.smsbox.domain.repositories.PlaceholdersRepository
import com.brainymobile.android.smsbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import kotlinx.coroutines.flow.Flow

class FetchPlaceholdersUseCaseImpl(private val repository: PlaceholdersRepository) :
    FetchPlaceholdersUseCase {

    override fun getPlaceholders(): Flow<List<Placeholder>> {
        return repository.getPlaceholdersObservable()
    }

    override suspend fun getValue(name: String): String? {
        return repository.getValue(name)
    }

    override suspend fun getByName(name: String): Placeholder? {
        return repository.getByName(name)
    }

    override suspend fun getById(id: Int): Placeholder? {
        return repository.getById(id)
    }
}