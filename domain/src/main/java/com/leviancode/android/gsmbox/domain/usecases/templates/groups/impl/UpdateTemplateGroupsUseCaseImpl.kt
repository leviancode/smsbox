package com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.repositories.TemplateGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.UpdateTemplateGroupsUseCase

class UpdateTemplateGroupsUseCaseImpl(private val repository: TemplateGroupsRepository) : UpdateTemplateGroupsUseCase {
    override suspend fun update(item: TemplateGroup) {
        repository.update(item)
    }

    override suspend fun update(items: List<TemplateGroup>) {
        repository.update(items)
    }
}