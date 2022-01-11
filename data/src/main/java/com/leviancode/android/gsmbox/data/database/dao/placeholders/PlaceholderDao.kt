package com.leviancode.android.gsmbox.data.database.dao.placeholders

import androidx.room.*
import com.leviancode.android.gsmbox.data.entities.placeholders.PlaceholderData
import com.leviancode.android.gsmbox.data.entities.placeholders.toPlaceholderData
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceholderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PlaceholderData)
    @Update
    suspend fun update(item: PlaceholderData)
    @Delete
    suspend fun delete(vararg item: PlaceholderData)

    @Transaction
    suspend fun upsert(item: PlaceholderData){
        val placeholder = get(item.id)
        if (placeholder == null) insert(item)
        else update(item)
    }

    @Query("SELECT * FROM placeholders")
    fun getAll(): Flow<List<PlaceholderData>>


    @Query("SELECT value FROM placeholders WHERE name = :name")
    suspend fun getValueByName(name: String): String?

    @Query("SELECT * FROM placeholders WHERE id = :id")
    suspend fun get(id: Int): PlaceholderData?

    @Query("SELECT * FROM placeholders WHERE name = :name")
    suspend fun getByName(name: String): PlaceholderData?

    @Query("DELETE FROM placeholders")
    suspend fun deleteAll()

    @Query("DELETE FROM placeholders WHERE id =:id")
    fun deleteById(id: Int)
}