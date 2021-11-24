package com.leviancode.android.gsmbox.data.entities.recipients

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups

fun RecipientData.toDomainRecipient() = Recipient(
    id = this.recipientId,
    name = this.name,
    phoneNumber = this.phoneNumber
)

fun Recipient.toRecipientData() = RecipientData(
    recipientId = this.id,
    name = this.name,
    phoneNumber = this.phoneNumber
)

fun List<RecipientData>.toDomainRecipients() = map { it.toDomainRecipient() }
fun List<RecipientWithGroupsData>.toDomainRecipientsWithGroups() = map { it.toDomainRecipientWithGroups() }
fun List<Recipient>.toRecipientsData() = map { it.toRecipientData() }
fun List<GroupWithRecipients>.toDomainRecipientGroupsWithRecipients() = map { it.toDomainRecipientGroup() }

fun List<RecipientGroupData>.toDomainRecipientGroups() = map { it.toDomainRecipientGroup() }
fun List<RecipientGroup>.toRecipientGroupData() = map { it.toRecipientGroupData() }

fun GroupWithRecipients.toDomainRecipientGroup() = RecipientGroup(
    id = group.recipientGroupId,
    name = group.name,
    iconColor = group.iconColor,
    recipients = this.recipients.toDomainRecipients()
)
fun RecipientWithGroupsData.toDomainRecipient() = Recipient(
    id = recipient.recipientId,
    name = recipient.name,
    phoneNumber = recipient.phoneNumber,
)

fun RecipientWithGroupsData.toDomainRecipientWithGroups() = RecipientWithGroups(
    recipient = recipient.toDomainRecipient(),
    groups = groups.toDomainRecipientGroups()
)

fun RecipientWithGroups.toRecipientWithGroupsData() = RecipientWithGroupsData(
    recipient = recipient.toRecipientData(),
    groups = groups.toRecipientGroupData()
)

fun RecipientGroup.toRecipientGroupData() = RecipientGroupData(
    recipientGroupId = id,
    name = this.name,
    iconColor = this.iconColor
)

fun RecipientGroupData.toDomainRecipientGroup() = RecipientGroup(
    id = recipientGroupId,
    name = this.name,
    iconColor = this.iconColor,
    recipients = listOf()
)

fun RecipientGroup.toDataGroupWithRecipients() = GroupWithRecipients(
    group = toRecipientGroupData(),
    recipients = recipients.toRecipientsData()
)