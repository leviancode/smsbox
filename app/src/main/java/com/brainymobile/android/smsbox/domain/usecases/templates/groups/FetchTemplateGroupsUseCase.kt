package com.brainymobile.android.smsbox.domain.usecases.templates.groups

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import kotlinx.coroutines.flow.Flow

interface FetchTemplateGroupsUseCase {
    fun getGroupsObservable(): Flow<List<TemplateGroup>>
    suspend fun getById(id: Int): TemplateGroup?
    suspend fun getByName(name: String): TemplateGroup?
}