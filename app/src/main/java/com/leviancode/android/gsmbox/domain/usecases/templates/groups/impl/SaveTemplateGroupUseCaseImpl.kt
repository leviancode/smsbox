package com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.repositories.TemplateGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.SaveTemplateGroupUseCase

class SaveTemplateGroupUseCaseImpl(private val repository: TemplateGroupsRepository) :
    SaveTemplateGroupUseCase {

    override suspend fun save(item: TemplateGroup) {
        val updatedItem = updatePosition(item)
        repository.save(updatedItem)
    }

    override suspend fun save(items: List<TemplateGroup>) {
        items.forEach { save(it) }
    }

    private suspend fun updatePosition(item: TemplateGroup): TemplateGroup {
        return item.apply {
            position = repository.count()
        }
    }
}