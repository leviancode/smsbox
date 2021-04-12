package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.templates.TemplateAndRecipientRelation
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients

@Dao
interface TemplateAndRecipientRelationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TemplateAndRecipientRelation)

    @Delete
    suspend fun delete(vararg item: TemplateAndRecipientRelation)

    @Query("DELETE FROM templates_and_recipients WHERE templateId = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    @Query("DELETE FROM templates_and_recipients WHERE recipientId = :recipientId")
    suspend fun deleteByRecipientId(recipientId: Long)

    @Transaction
    @Query("SELECT * FROM templates")
    fun getTemplateWithRecipients(): LiveData<List<TemplateWithRecipients>>
}