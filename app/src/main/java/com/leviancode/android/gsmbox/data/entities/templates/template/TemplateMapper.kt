package com.leviancode.android.gsmbox.data.entities.templates.template

import com.leviancode.android.gsmbox.data.entities.recipients.toDomainRecipientGroup
import com.leviancode.android.gsmbox.data.entities.recipients.toDomainRecipients
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.entities.template.Template

fun TemplateWithRecipients.toDomainTemplate() = Template(
    id = template.templateId,
    position = template.position,
    groupId = template.templateGroupId,
    name = template.name,
    message = template.message,
    iconColor = template.iconColor,
    favorite = template.favorite,
    recipientGroup = recipients?.group?.toDomainRecipientGroup(
        recipients?.recipients?.toDomainRecipients() ?: listOf()
    ) ?: RecipientGroup(),
    timestamp = template.timestamp
)


fun Template.toTemplateData() = TemplateData(
    templateId = id,
    position = position,
    templateGroupId = groupId,
    name = name,
    message = message,
    iconColor = iconColor,
    favorite = favorite,
    timestamp = timestamp
)

fun Template.toTemplateData(recipientGroupId: Int) = TemplateData(
    templateId = id,
    position = position,
    templateGroupId = groupId,
    recipientGroupId = recipientGroupId,
    name = name,
    message = message,
    iconColor = iconColor,
    favorite = favorite,
    timestamp = timestamp
)

fun List<TemplateWithRecipients>.toDomainTemplates() = map { it.toDomainTemplate() }
fun List<Template>.toDataTemplates() = map { it.toTemplateData() }