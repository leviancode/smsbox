package com.leviancode.android.gsmbox.ui.entities.recipients

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups

fun RecipientGroup.toRecipientGroupUI() = RecipientGroupUI(
    id = this.id,
    name = this.name,
    iconColor = this.iconColor,
    recipients = this.recipients.toRecipientsUI().toMutableList()
)

fun RecipientGroupUI.toDomainRecipientGroup() = RecipientGroup(
    id = this.id,
    name = getName(),
    iconColor = getIconColor(),
    recipients = recipients.toDomainRecipients().toMutableList()
)

fun Recipient.toRecipientUI() = RecipientUI(
    id = this.id,
    name = this.name,
    phoneNumber = this.phoneNumber,
)

fun RecipientUI.toDomainRecipient() = Recipient(
    id = this.id,
    name = getName(),
    phoneNumber = getPhoneNumber()
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