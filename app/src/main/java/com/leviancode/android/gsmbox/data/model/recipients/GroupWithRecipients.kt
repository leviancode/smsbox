package com.leviancode.android.gsmbox.data.model.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation

data class GroupWithRecipients(
    @Embedded val group: RecipientGroup,
    @Relation(
        parentColumn = "recipientGroupId",
        entityColumn = "recipientId",
        associateBy = Junction(RecipientsAndGroupsCrossRef::class)
    )
    var recipients: List<Recipient>
) : BaseObservable() {

    @Bindable
    fun getSizeAsString() = recipients.size.toString()
}
