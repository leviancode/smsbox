package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchTemplatesUseCaseImpl(private val repository: TemplatesRepository) :
    FetchTemplatesUseCase {

    override fun getFavoritesObservable(): Flow<List<Template>> {
        return repository.getFavoritesObservable()
    }

    override fun getFavoritesSync(): List<Template> {
        return repository.getFavoritesSync()
    }

    override fun getTemplatesByGroupIdObservable(groupId: Int): Flow<List<Template>> {
        return repository.getTemplatesByGroupIdObservable(groupId).map { it.sortedBy { it.position } }
    }

    override suspend fun get(id: Int): Template? {
        return repository.getById(id)
    }

    override suspend fun getByName(name: String): Template? {
        return repository.getByName(name)
    }
}