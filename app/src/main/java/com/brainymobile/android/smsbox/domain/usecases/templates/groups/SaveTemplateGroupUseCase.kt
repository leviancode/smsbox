package com.brainymobile.android.smsbox.domain.usecases.templates.groups

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup

interface SaveTemplateGroupUseCase {
    suspend fun save(item: TemplateGroup)
    suspend fun save(items: List<TemplateGroup>)
}