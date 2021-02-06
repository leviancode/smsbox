package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.Recipient

@Dao
interface RecipientDao {
    @Insert suspend fun insert(vararg item: Recipient)
    @Update suspend fun update(vararg item: Recipient)
    @Delete suspend fun delete(vararg item: Recipient)

    @Query("SELECT * from recipients WHERE recipient_id = :id")
    suspend fun get(id: String): Recipient?

    @Query("SELECT * FROM recipients")
    fun getAll(): LiveData<List<Recipient>>
}