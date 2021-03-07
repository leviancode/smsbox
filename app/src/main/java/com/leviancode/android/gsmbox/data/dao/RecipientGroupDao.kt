package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroupWithRecipients

@Dao
interface RecipientGroupDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: RecipientGroup)
    @Update
    suspend fun update(vararg item: RecipientGroup)
    @Delete
    suspend fun delete(vararg item: RecipientGroup)

    @Query("SELECT * from recipient_groups WHERE groupId = :id")
    suspend fun getById(id: String): RecipientGroup?

    @Query("SELECT * from recipient_groups WHERE groupName = :name")
    suspend fun getByName(name: String): RecipientGroup?

    @Query("SELECT * FROM recipient_groups")
    fun getAll(): LiveData<List<RecipientGroup>>

    @Transaction
    @Query("SELECT * FROM recipient_groups")
    fun getGroupsWithRecipients(): LiveData<List<RecipientGroupWithRecipients>>
}