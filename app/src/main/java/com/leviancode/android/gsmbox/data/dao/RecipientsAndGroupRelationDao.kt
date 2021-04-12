package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.data.model.recipients.RecipientsAndGroupRelation

@Dao
interface RecipientsAndGroupRelationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: RecipientsAndGroupRelation)

    @Delete
    suspend fun delete(vararg item: RecipientsAndGroupRelation)

    @Query("DELETE FROM recipients_and_groups WHERE recipientGroupId = :groupId")
    suspend fun deleteByGroupId(groupId: Long)

    @Query("DELETE FROM recipients_and_groups WHERE recipientId = :recipientId")
    suspend fun deleteByRecipientId(recipientId: Long)

    @Transaction
    @Query("SELECT * FROM recipients")
    fun getRecipientsWithGroups(): LiveData<List<RecipientWithGroups>>

    @Transaction
    @Query("SELECT * FROM recipient_groups")
    fun getGroupsWithRecipients(): LiveData<List<GroupWithRecipients>>

    @Transaction
    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId =:groupId")
    suspend fun getGroupWithRecipients(groupId: Long): GroupWithRecipients?

    @Transaction
    @Query("SELECT * FROM recipients WHERE recipientId = :recipientId")
    suspend fun getRecipientWithGroups(recipientId: Long): RecipientWithGroups?
}