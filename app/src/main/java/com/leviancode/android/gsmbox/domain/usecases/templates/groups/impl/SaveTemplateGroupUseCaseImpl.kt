package com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.repositories.TemplateGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.SaveTemplateGroupUseCase

class SaveTemplateGroupUseCaseImpl(private val repository: TemplateGroupsRepository) :
    SaveTemplateGroupUseCase {
    override suspend fun save(item: TemplateGroup) {
       repository.save(item)
    }
}