package com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import com.brainymobile.android.smsbox.domain.repositories.TemplateGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.UpdateTemplateGroupsUseCase

class UpdateTemplateGroupsUseCaseImpl(private val repository: TemplateGroupsRepository) : UpdateTemplateGroupsUseCase {
    override suspend fun update(item: TemplateGroup) {
        repository.update(item)
    }

    override suspend fun update(items: List<TemplateGroup>) {
        repository.update(items)
    }
}