package com.leviancode.android.gsmbox.data.entities.recipients

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups

fun RecipientData.toDomainRecipient() = Recipient(
    id = this.recipientId,
    position = position,
    name = this.name,
    phoneNumber = this.phoneNumber,
    timestamp = timestamp
)

fun Recipient.toRecipientData() = RecipientData(
    recipientId = this.id,
    position = position,
    name = this.name,
    phoneNumber = this.phoneNumber,
    timestamp = timestamp
)

fun List<RecipientData>.toDomainRecipients() = map { it.toDomainRecipient() }
fun List<RecipientWithGroupsData>.toDomainRecipientsWithGroups() = map { it.toDomainRecipientWithGroups() }
fun List<Recipient>.toRecipientsData() = map { it.toRecipientData() }
fun List<GroupWithRecipients>.toDomainRecipientGroupsWithRecipients() = map { it.toDomainRecipientGroup() }

fun List<RecipientGroupData>.toDomainRecipientGroups() = map { it.toDomainRecipientGroup() }
fun List<RecipientGroup>.toRecipientGroupData() = map { it.toRecipientGroupData() }

fun GroupWithRecipients.toDomainRecipientGroup() = RecipientGroup(
    id = group.recipientGroupId,
    position = group.position,
    name = group.name,
    iconColor = group.iconColor,
    recipients = recipients.toDomainRecipients(),
    timestamp = group.timestamp
)
fun RecipientWithGroupsData.toDomainRecipient() = Recipient(
    id = recipient.recipientId,
    position = recipient.position,
    name = recipient.name,
    phoneNumber = recipient.phoneNumber,
    timestamp = recipient.timestamp
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
    position = position,
    name = name,
    iconColor = iconColor,
    timestamp = timestamp
)

fun RecipientGroupData.toDomainRecipientGroup(recipients: List<Recipient> = listOf()) = RecipientGroup(
    id = recipientGroupId,
    position = position,
    name = name,
    iconColor = iconColor,
    recipients = recipients,
    timestamp = timestamp
)

fun RecipientGroup.toDataGroupWithRecipients() = GroupWithRecipients(
    group = toRecipientGroupData(),
    recipients = recipients.toRecipientsData()
)