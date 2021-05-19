package com.leviancode.android.gsmbox.core.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.core.data.dao.PlaceholderDao
import com.leviancode.android.gsmbox.core.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.core.utils.HASHTAG_REGEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

object PlaceholdersRepository {
    private val placeholderDao: PlaceholderDao
        get() = AppDatabase.instance.placeholderDao()

    fun getPlaceholders(): LiveData<List<Placeholder>> = placeholderDao.getAll()

    fun getNewPlaceholder()  = Placeholder()

    suspend fun deletePlaceholder(item: Placeholder) = withContext(Dispatchers.IO){
        placeholderDao.delete(item)
    }

    suspend fun savePlaceholder(item: Placeholder) = withContext(Dispatchers.IO){
        val placeholder = getPlaceholderById(item.placeholderId)
        if (placeholder == null) placeholderDao.insert(item)
        else placeholderDao.update(item)
    }

    suspend fun getPlaceholderById(id: Int): Placeholder? = withContext(Dispatchers.IO) {
        placeholderDao.get(id)
    }

    suspend fun getValueByName(name: String) = placeholderDao.getValueByName(name.trim())

    suspend fun getByName(name: String): Placeholder? {
        return placeholderDao.getByName(name)
    }

    suspend fun replaceHashtagNamesToValues(str: String): String {
        val matcher = Pattern.compile(HASHTAG_REGEX).matcher(str)
        if (matcher.find()) {
            val hashTag = matcher.group(1) ?: ""
            val value = getValueByName(hashTag) ?: return hashTag
            if (hashTag != value) {
                val newStr = str.replaceFirst(hashTag, value)
                return replaceHashtagNamesToValues(newStr)
            }
        }
        return str
    }
}