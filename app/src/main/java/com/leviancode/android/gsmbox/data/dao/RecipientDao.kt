package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.Recipient

@Dao
interface RecipientDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: Recipient)
    @Update suspend fun update(vararg item: Recipient)
    @Delete suspend fun delete(vararg item: Recipient)

    @Query("SELECT * FROM recipients WHERE recipientId = :id")
    suspend fun get(id: String): Recipient?

    @Query("SELECT * FROM recipients")
    fun getAllLiveData(): LiveData<List<Recipient>>

    @Query("SELECT * FROM recipients")
    fun getAll(): List<Recipient>

    @Query("DELETE FROM recipients WHERE groupName = :name")
    suspend fun deleteByGroupName(name: String)

    @Query("SELECT * FROM recipients WHERE groupName =:groupName")
    suspend fun getByGroupName(groupName: String): List<Recipient>

    @Query("UPDATE recipients SET groupName = null WHERE groupName =:groupName")
    suspend fun deleteGroupFromAll(groupName: String)
}