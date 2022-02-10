package com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl

import com.brainymobile.android.smsbox.domain.entities.template.Template
import com.brainymobile.android.smsbox.domain.repositories.TemplatesRepository
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.DeleteTemplatesUseCase
import javax.inject.Inject

class DeleteTemplatesUseCaseImpl @Inject constructor(private val repository: TemplatesRepository) :
    DeleteTemplatesUseCase {
    override suspend fun delete(item: Template) {
        repository.delete(item)
    }
}