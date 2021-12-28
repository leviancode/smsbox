package com.leviancode.android.gsmbox.domain.usecases.templates.groups

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup

interface UpdateTemplateGroupsUseCase {
    suspend fun update(items: List<TemplateGroup>)
}