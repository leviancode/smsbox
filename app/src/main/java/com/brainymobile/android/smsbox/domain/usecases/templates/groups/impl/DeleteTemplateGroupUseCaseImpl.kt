package com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import com.brainymobile.android.smsbox.domain.repositories.TemplateGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.DeleteTemplateGroupUseCase
import javax.inject.Inject

class DeleteTemplateGroupUseCaseImpl @Inject constructor(private val repository: TemplateGroupsRepository) :
    DeleteTemplateGroupUseCase {
    override suspend fun delete(item: TemplateGroup) {
        repository.delete(item)
    }
}