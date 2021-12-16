package com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.repositories.TemplateGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase

class FetchTemplateGroupsUseCaseImpl(private val repository: TemplateGroupsRepository) :
    FetchTemplateGroupsUseCase {

    override fun getGroupsObservable() = repository.getGroupsObservable()

    override suspend fun getById(id: Int): TemplateGroup? {
        return repository.getById(id)
    }

    override suspend fun getByName(name: String): TemplateGroup? {
        return repository.getByName(name)
    }
}