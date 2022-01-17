package com.brainymobile.android.smsbox.domain.usecases.templates.tempates

import com.brainymobile.android.smsbox.domain.entities.template.Template
import kotlinx.coroutines.flow.Flow

interface FetchTemplatesUseCase {
    fun getFavoritesObservable(): Flow<List<Template>>
    fun getFavoritesSync(): List<Template>
    fun getTemplatesByGroupIdObservable(groupId: Int): Flow<List<Template>>
    suspend fun get(id: Int): Template?
    suspend fun getByName(name: String): Template?
}