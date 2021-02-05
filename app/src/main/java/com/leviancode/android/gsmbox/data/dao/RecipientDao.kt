package com.leviancode.android.gsmbox.data.dao

import androidx.room.*
import com.leviancode.android.gsmbox.data.model.Recipient

@Dao
interface RecipientDao {
    @Insert
    fun insert(vararg template: Recipient)
    @Update
    fun update(vararg template: Recipient)
    @Delete
    fun delete(vararg template: Recipient)

    @Query("SELECT * FROM recipients")
    fun getAll(): List<Recipient>
}