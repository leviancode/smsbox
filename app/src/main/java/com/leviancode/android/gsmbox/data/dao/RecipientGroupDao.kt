package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients

@Dao
interface RecipientGroupDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: RecipientGroup): Long
    @Update
    suspend fun update(item: RecipientGroup)
    @Delete
    suspend fun delete(vararg item: RecipientGroup)

    @Query("SELECT * from recipient_groups WHERE recipientGroupId = :id")
    suspend fun getById(id: Long): RecipientGroup?

    @Query("SELECT * from recipient_groups WHERE name = :name")
    suspend fun getByName(name: String): RecipientGroup?

    @Query("SELECT * FROM recipient_groups WHERE name is not null")
    fun getAllLiveData(): LiveData<List<RecipientGroup>>

    @Query("SELECT * FROM recipient_groups")
    fun getAll(): List<RecipientGroup>
}