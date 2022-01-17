package com.brainymobile.android.smsbox.domain.repositories

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import kotlinx.coroutines.flow.Flow

interface TemplateGroupsRepository {

    fun getGroupsObservable(): Flow<List<TemplateGroup>>

    fun getGroupNames(): Flow<List<String>>

    suspend fun getById(id: Int): TemplateGroup?

    suspend fun getByName(name: String): TemplateGroup?

    suspend fun save(item: TemplateGroup): Int

    suspend fun save(items: List<TemplateGroup>): IntArray

    suspend fun delete(item: TemplateGroup)

    suspend fun count(): Int

    suspend fun update(items: List<TemplateGroup>)

    suspend fun update(item: TemplateGroup)

}