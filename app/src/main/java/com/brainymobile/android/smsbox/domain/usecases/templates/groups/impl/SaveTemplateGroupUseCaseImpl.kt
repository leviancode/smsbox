package com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import com.brainymobile.android.smsbox.domain.repositories.TemplateGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.SaveTemplateGroupUseCase
import javax.inject.Inject

class SaveTemplateGroupUseCaseImpl @Inject constructor(private val repository: TemplateGroupsRepository) :
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