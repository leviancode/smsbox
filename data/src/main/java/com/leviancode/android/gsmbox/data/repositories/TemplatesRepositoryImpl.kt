package com.leviancode.android.gsmbox.data.repositories

import com.leviancode.android.gsmbox.data.database.dao.templates.TemplateDao
import com.leviancode.android.gsmbox.data.entities.templates.template.toDataTemplates
import com.leviancode.android.gsmbox.data.entities.templates.template.toDomainTemplate
import com.leviancode.android.gsmbox.data.entities.templates.template.toDomainTemplates
import com.leviancode.android.gsmbox.data.entities.templates.template.toTemplateData
import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.RecipientGroupsRepository
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TemplatesRepositoryImpl(
    private val dao: TemplateDao,
    private val recipientGroupsRepository: RecipientGroupsRepository
) : TemplatesRepository {

    override fun getTemplatesByGroupIdObservable(groupId: Int): Flow<List<Template>> {
        return dao.getTemplatesWithRecipients(groupId).map { it.toDomainTemplates() }
    }

    override suspend fun getByName(name: String) =
        dao.getTemplateWithRecipientsByName(name)?.toDomainTemplate()

    override fun getFavoritesObservable() =
        dao.getFavoriteWithRecipientsObservable().map { it.toDomainTemplates() }

    override suspend fun getFavorites() =
        dao.getFavoriteWithRecipients().map { it.toDomainTemplate() }

    override fun getFavoritesSync() =
        dao.getFavoriteWithRecipientsSync().map { it.toDomainTemplate() }

    override fun getTemplateNamesExclusiveById(id: Int) = dao.getTemplateNamesExclusiveById(id)

    override suspend fun getById(templateId: Int) = withContext(IO) {
        dao.getTemplateWithRecipientsById(templateId)?.toDomainTemplate()
    }

    override suspend fun save(item: Template): Int {
        val recipientGroupId = recipientGroupsRepository.save(item.recipientGroup)
        val data = item.toTemplateData(recipientGroupId)
        return dao.upsert(data)
    }

    override suspend fun updateFavorite(templateId: Int, favorite: Boolean) = withContext(IO) {
        dao.updateFavorite(templateId, favorite)
    }

    override suspend fun delete(item: Template) = withContext(IO) {
        if (item.recipientGroup.name == null) {
            recipientGroupsRepository.delete(item.recipientGroup)
        }
        dao.delete(item.toTemplateData())
    }

    override suspend fun deleteByGroupId(groupId: Int) = withContext(IO) {
        dao.deleteByGroupId(groupId)
    }

    override suspend fun update(items: List<Template>) = withContext(IO) {
        val data = items.toDataTemplates()
        dao.update(*data.toTypedArray())
    }

    override suspend fun update(item: Template) {
        dao.update(item.toTemplateData())
    }

    override suspend fun count(): Int = withContext(IO) {
        dao.count()
    }

}


