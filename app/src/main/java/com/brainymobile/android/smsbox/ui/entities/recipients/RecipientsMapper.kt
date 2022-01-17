package com.brainymobile.android.smsbox.ui.entities.recipients

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups

fun RecipientGroup.toRecipientGroupUI() = RecipientGroupUI(
    id = this.id,
    position = position,
    name = this.name,
    iconColor = this.iconColor,
    recipients = this.recipients.toRecipientsUI().toMutableList(),
    timestamp = timestamp
)

fun RecipientGroupUI.toDomainRecipientGroup() = RecipientGroup(
    id = this.id,
    position = position,
    name = getName(),
    iconColor = getIconColor(),
    recipients = recipients.toDomainRecipients().toMutableList(),
    timestamp = timestamp
)

fun Recipient.toRecipientUI() = RecipientUI(
    id = this.id,
    position = position,
    name = this.name,
    phoneNumber = this.phoneNumber,
    timestamp = timestamp
)

fun RecipientUI.toDomainRecipient() = Recipient(
    id = this.id,
    position = position,
    name = getName(),
    phoneNumber = getPhoneNumber(),
    timestamp = timestamp
)

fun RecipientWithGroupsUI.toDomainRecipientWithGroups() = RecipientWithGroups(
    recipient = recipient.toDomainRecipient(),
    groups = groups.toDomainRecipientGroups()
)

fun RecipientWithGroups.toUIRecipientWithGroups() = RecipientWithGroupsUI(
    recipient = recipient.toRecipientUI(),
    groups = groups.toRecipientGroupsUI().toMutableList()
)

fun List<Recipient>.toRecipientsUI() = map { it.toRecipientUI() }
fun List<RecipientUI>.toDomainRecipients() = map { it.toDomainRecipient() }
fun List<RecipientGroup>.toRecipientGroupsUI() = map { it.toRecipientGroupUI() }
fun List<RecipientGroupUI>.toDomainRecipientGroups() = map { it.toDomainRecipientGroup() }
fun List<RecipientWithGroups>.toRecipientsWithGroupsUI() = map { it.toUIRecipientWithGroups() }