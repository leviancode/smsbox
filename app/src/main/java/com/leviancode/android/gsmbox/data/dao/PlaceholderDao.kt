package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder

@Dao
interface PlaceholderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: Placeholder)
    @Update
    suspend fun update(vararg item: Placeholder)
    @Delete
    suspend fun delete(vararg item: Placeholder)

    @Query("SELECT * FROM placeholders")
    fun getAll(): LiveData<List<Placeholder>>

    @Query("SELECT * FROM placeholders WHERE placeholderId = :id")
    suspend fun get(id: String): Placeholder?

    @Query("SELECT value FROM placeholders WHERE keyword = :key")
    suspend fun getValueByKey(key: String): String?

    @Query("SELECT * FROM placeholders WHERE placeholderId = :id")
    suspend fun getById(id: String): Placeholder?

    @Query("DELETE FROM placeholders")
    suspend fun deleteAll()
}