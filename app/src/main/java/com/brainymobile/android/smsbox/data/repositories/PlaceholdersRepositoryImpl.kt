package com.brainymobile.android.smsbox.data.repositories

import com.brainymobile.android.smsbox.data.database.dao.placeholders.PlaceholderDao
import com.brainymobile.android.smsbox.data.entities.placeholders.toDomainPlaceholder
import com.brainymobile.android.smsbox.data.entities.placeholders.toDomainPlaceholders
import com.brainymobile.android.smsbox.data.entities.placeholders.toPlaceholderData
import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder
import com.brainymobile.android.smsbox.domain.repositories.PlaceholdersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceholdersRepositoryImpl @Inject constructor(private val dao: PlaceholderDao): PlaceholdersRepository {

    override fun getPlaceholdersObservable(): Flow<List<Placeholder>> =
        dao.getAll().map { it.toDomainPlaceholders() }

    override suspend fun getById(id: Int): Placeholder? = withContext(Dispatchers.IO) {
        dao.get(id)?.toDomainPlaceholder()
    }

    override suspend fun getValue(name: String)  = withContext(Dispatchers.IO) {
        dao.getValueByName(name.trim())
    }

    override suspend fun getByName(name: String) = withContext(Dispatchers.IO) {
        dao.getByName(name)?.toDomainPlaceholder()
    }

    override suspend fun delete(item: Placeholder) = withContext(Dispatchers.IO){
        dao.delete(item.toPlaceholderData())
    }

    override suspend fun deleteById(id: Int) = withContext(Dispatchers.IO){
        dao.deleteById(id)
    }

    override suspend fun save(item: Placeholder) = withContext(Dispatchers.IO){
       dao.upsert(item.toPlaceholderData())
    }
}