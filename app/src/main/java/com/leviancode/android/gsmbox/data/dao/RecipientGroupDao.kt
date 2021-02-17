package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.data.model.RecipientGroupAndRecipients

@Dao
interface RecipientGroupDao {
    @Insert
    suspend fun insert(vararg item: RecipientGroup)
    @Update
    suspend fun update(vararg item: RecipientGroup)
    @Delete
    suspend fun delete(vararg item: RecipientGroup)

    @Query("SELECT * from recipient_groups WHERE group_id = :id")
    suspend fun get(id: String): RecipientGroup?

    @Query("SELECT * FROM recipient_groups")
    fun getAll(): LiveData<List<RecipientGroup>>

    @Transaction
    @Query("SELECT * FROM recipient_groups")
    fun getGroupsWithRecipients(): LiveData<List<RecipientGroupAndRecipients>>
}