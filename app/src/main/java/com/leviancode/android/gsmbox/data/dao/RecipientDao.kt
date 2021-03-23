package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups

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
}