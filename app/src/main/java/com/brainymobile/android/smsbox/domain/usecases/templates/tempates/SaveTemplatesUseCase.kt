package com.brainymobile.android.smsbox.domain.usecases.templates.tempates

import com.brainymobile.android.smsbox.domain.entities.template.Template

interface SaveTemplatesUseCase {
    suspend fun save(item: Template)
    suspend fun save(items: List<Template>)
}