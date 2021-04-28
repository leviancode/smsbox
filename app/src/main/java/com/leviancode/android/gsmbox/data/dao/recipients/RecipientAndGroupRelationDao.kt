package com.leviancode.android.gsmbox.data.dao.recipients

import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientsAndGroupRelation

@Dao
interface RecipientAndGroupRelationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg item: RecipientsAndGroupRelation)

    @Query("SELECT * FROM recipients_and_groups WHERE recipientGroupId = :groupId AND recipientId =:recipientId")
    suspend fun get(groupId: Int, recipientId: Int): RecipientsAndGroupRelation?

    @Delete
    suspend fun delete(vararg item: RecipientsAndGroupRelation)

    @Query("DELETE FROM recipients_and_groups WHERE recipientGroupId = :groupId")
    suspend fun deleteByGroupId(groupId: Int)

    @Query("DELETE FROM recipients_and_groups WHERE recipientId = :recipientId")
    suspend fun deleteByRecipientId(recipientId: Int)

    @Query("SELECT recipientGroupId FROM recipients_and_groups WHERE recipientId = :recipientId")
    suspend fun getGroupsWithRecipientRelation(recipientId: Int): List<Int>
}