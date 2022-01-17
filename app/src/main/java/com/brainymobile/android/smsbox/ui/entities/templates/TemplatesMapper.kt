package com.brainymobile.android.smsbox.ui.entities.templates

import com.brainymobile.android.smsbox.domain.entities.template.Template
import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup
import com.brainymobile.android.smsbox.ui.entities.recipients.toDomainRecipientGroup
import com.brainymobile.android.smsbox.ui.entities.recipients.toRecipientGroupUI

fun Template.toTemplateUI() = TemplateUI(
    id = this.id,
    position = position,
    groupId = this.groupId,
    name = this.name,
    message = this.message,
    iconColor = this.iconColor,
    recipientGroup = this.recipientGroup.toRecipientGroupUI(),
    favorite = this.favorite,
    timestamp = timestamp
)

fun TemplateUI.toDomainTemplate() = Template(
    id = this.id,
    position = position,
    groupId = this.groupId,
    name = getName(),
    message = getMessage(),
    iconColor = getIconColor(),
    recipientGroup = this.recipientGroup.toDomainRecipientGroup(),
    favorite = isFavorite(),
    timestamp = timestamp
)

fun TemplateGroupUI.toDomainTemplateGroup() = TemplateGroup(
    id = this.id,
    position = position,
    name = getName(),
    iconColor = getIconColor(),
    description = getDescription(),
    size = size,
    timestamp = timestamp
)

fun TemplateGroup.toTemplateGroupUI() = TemplateGroupUI(
    id = this.id,
    position = position,
    name = name,
    iconColor = iconColor,
    description = description,
    size = size,
    timestamp = timestamp
)

fun List<Template>.toUITemplates() = map { it.toTemplateUI() }
fun List<TemplateUI>.toDomainTemplates() = map { it.toDomainTemplate() }
fun List<TemplateGroup>.toTemplateGroupsUI() = map { it.toTemplateGroupUI() }
fun List<TemplateGroupUI>.toDomainTemplateGroups() = map { it.toDomainTemplateGroup() }