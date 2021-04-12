package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.PlaceholderDao
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PlaceholdersRepository {
    private val placeholderDao: PlaceholderDao
        get() = AppDatabase.instance.placeholderDao()

    val data: LiveData<List<Placeholder>> = placeholderDao.getAll()

    fun getNewPlaceholder()  = Placeholder()

    suspend fun deletePlaceholder(item: Placeholder) = withContext(Dispatchers.IO){
        placeholderDao.delete(item)
    }

    suspend fun savePlaceholder(item: Placeholder) = withContext(Dispatchers.IO){
        val placeholder = getPlaceholderById(item.placeholderId)
        if (placeholder == null) placeholderDao.insert(item)
        else placeholderDao.update(item)
    }

    suspend fun getPlaceholderById(id: Long): Placeholder? = withContext(Dispatchers.IO) {
        placeholderDao.get(id)
    }

    suspend fun getValueByName(name: String) = withContext(Dispatchers.IO) {
        placeholderDao.getValueByName(name.trim())
    }

    suspend fun getByName(name: String): Placeholder? {
        return placeholderDao.getByName(name)
    }
}