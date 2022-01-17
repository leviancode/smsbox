package com.brainymobile.android.smsbox.domain.usecases.templates.tempates

import com.brainymobile.android.smsbox.domain.entities.template.Template

interface DeleteTemplatesUseCase {
    suspend fun delete(item: Template)
}