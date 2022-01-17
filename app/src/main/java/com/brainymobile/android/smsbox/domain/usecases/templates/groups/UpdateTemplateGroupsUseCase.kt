package com.brainymobile.android.smsbox.domain.usecases.templates.groups

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup

interface UpdateTemplateGroupsUseCase {
    suspend fun update(item: TemplateGroup)
    suspend fun update(items: List<TemplateGroup>)
}