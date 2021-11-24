package com.leviancode.android.gsmbox.domain.usecases.templates.tempates

import com.leviancode.android.gsmbox.domain.entities.template.Template

interface DeleteTemplatesUseCase {
    suspend fun delete(item: Template)
}