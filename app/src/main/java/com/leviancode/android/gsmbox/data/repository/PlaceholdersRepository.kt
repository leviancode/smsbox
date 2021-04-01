package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.PlaceholderDao
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PlaceholdersRepository {
    private val placeholderDao: PlaceholderDao
        get() = AppDatabase.INSTANCE.placeholderDao()

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

    suspend fun getPlaceholderById(id: String): Placeholder? = withContext(Dispatchers.IO) {
        placeholderDao.getById(id)
    }

    suspend fun getValueByName(name: String) = withContext(Dispatchers.IO) {
        placeholderDao.getValueByName(name.trim())
    }
}