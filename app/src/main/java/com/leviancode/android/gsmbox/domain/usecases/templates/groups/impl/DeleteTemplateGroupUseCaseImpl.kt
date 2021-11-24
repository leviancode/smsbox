package com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.repositories.TemplateGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.DeleteTemplateGroupUseCase

class DeleteTemplateGroupUseCaseImpl(private val repository: TemplateGroupsRepository) :
    DeleteTemplateGroupUseCase {
    override suspend fun delete(item: TemplateGroup) {
        repository.delete(item)
    }
}