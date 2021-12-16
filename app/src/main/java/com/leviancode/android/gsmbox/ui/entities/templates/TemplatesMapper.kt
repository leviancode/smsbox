package com.leviancode.android.gsmbox.ui.entities.templates

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.ui.entities.recipients.toDomainRecipientGroup
import com.leviancode.android.gsmbox.ui.entities.recipients.toRecipientGroupUI

fun Template.toTemplateUI() = TemplateUI(
    id = this.id,
    groupId = this.groupId,
    name = this.name,
    message = this.message,
    iconColor = this.iconColor,
    recipientGroup = this.recipientGroup.toRecipientGroupUI(),
    favorite = this.favorite
)

fun TemplateUI.toDomainTemplate() = Template(
    id = this.id,
    groupId = this.groupId,
    name = getName(),
    message = getMessage(),
    iconColor = getIconColor(),
    recipientGroup = this.recipientGroup.toDomainRecipientGroup(),
    favorite = isFavorite()
)

fun TemplateGroupUI.toDomainTemplateGroup() = TemplateGroup(
    id = this.id,
    name = getName(),
    iconColor = getIconColor(),
    description = getDescription(),
    size = size
)

fun TemplateGroup.toTemplateGroupUI() = TemplateGroupUI(
    id = this.id,
    name = name,
    iconColor = iconColor,
    description = description,
    size = size
)

fun List<Template>.toUITemplates() = map { it.toTemplateUI() }
fun List<TemplateUI>.toDomainTemplates() = map { it.toDomainTemplate() }
fun List<TemplateGroup>.toTemplateGroupsUI() = map { it.toTemplateGroupUI() }