package com.brainymobile.android.smsbox.data.repositories

import com.brainymobile.android.smsbox.data.database.dao.templates.TemplateGroupDao
import com.brainymobile.android.smsbox.data.entities.templates.group.toDataGroup
import com.brainymobile.android.smsbox.data.entities.templates.group.toDataTemplateGroups
import com.brainymobile.android.smsbox.data.entities.templates.group.toDomainGroup
import com.brainymobile.android.smsbox.data.entities.templates.group.toDomainGroups
import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import com.brainymobile.android.smsbox.domain.repositories.TemplateGroupsRepository
import com.brainymobile.android.smsbox.domain.repositories.TemplatesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TemplateGroupsRepositoryImpl @Inject constructor(
    private val groupsDao: TemplateGroupDao,
    private val templatesRepository: TemplatesRepository
): TemplateGroupsRepository {

    override fun getGroupsObservable() = groupsDao.getGroupsWithTemplatesObservable().map { it.toDomainGroups() }

    override fun getGroupNames() = groupsDao.getGroupNames()

    override suspend fun getById(id: Int) = groupsDao.getGroupWithTemplates(id)?.toDomainGroup()

    override suspend fun getByName(name: String) = groupsDao.getGroupWithTemplatesByName(name)?.toDomainGroup()

    override suspend fun save(item: TemplateGroup) = withContext(IO) {
        groupsDao.upsert(item.toDataGroup())
    }

    override suspend fun save(items: List<TemplateGroup>) = withContext(IO) {
        items.map { save(it) }.toIntArray()
    }

    override suspend fun delete(item: TemplateGroup) = withContext(IO) {
        templatesRepository.deleteByGroupId(item.id)
        groupsDao.delete(item.toDataGroup())
    }

    override suspend fun count(): Int = withContext(IO) {
        groupsDao.count()
    }

    override suspend fun update(items: List<TemplateGroup>) = withContext(IO)  {
        groupsDao.update(*items.toDataTemplateGroups().toTypedArray())
    }

    override suspend fun update(item: TemplateGroup) {
        groupsDao.update(item.toDataGroup())
    }
}