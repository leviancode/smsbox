package com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import com.brainymobile.android.smsbox.domain.repositories.TemplateGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchTemplateGroupsUseCaseImpl @Inject constructor(private val repository: TemplateGroupsRepository) :
    FetchTemplateGroupsUseCase {

    override fun getGroupsObservable(): Flow<List<TemplateGroup>> =
        repository.getGroupsObservable()

    override suspend fun getById(id: Int): TemplateGroup? {
        return repository.getById(id)
    }

    override suspend fun getByName(name: String): TemplateGroup? {
        return repository.getByName(name)
    }
}