package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups

@Dao
interface RecipientDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Recipient): Long

    @Update suspend fun update(item: Recipient)

    @Delete suspend fun delete(item: Recipient)

    @Query("SELECT * FROM recipients WHERE recipientId = :id")
    suspend fun get(id: Long): Recipient?

    @Query("SELECT * FROM recipients WHERE name = :name")
    suspend fun getByName(name: String): Recipient?

    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getAllLiveData(): LiveData<List<Recipient>>

    @Query("SELECT * FROM recipients")
    fun getAll(): List<Recipient>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    fun getNumbersWithNotEmptyNamesLiveData(): LiveData<List<String>>
    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    suspend fun getNumbersWithNotEmptyNames(): List<String>
}