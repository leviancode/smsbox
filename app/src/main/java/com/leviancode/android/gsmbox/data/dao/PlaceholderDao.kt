package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder

@Dao
interface PlaceholderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Placeholder)
    @Update
    suspend fun update(item: Placeholder)
    @Delete
    suspend fun delete(vararg item: Placeholder)

    @Query("SELECT * FROM placeholders")
    fun getAll(): LiveData<List<Placeholder>>


    @Query("SELECT value FROM placeholders WHERE name = :name")
    suspend fun getValueByName(name: String): String?

    @Query("SELECT * FROM placeholders WHERE placeholderId = :id")
    suspend fun get(id: Long): Placeholder?

    @Query("SELECT * FROM placeholders WHERE name = :name")
    suspend fun getByName(name: String): Placeholder?

    @Query("DELETE FROM placeholders")
    suspend fun deleteAll()
}