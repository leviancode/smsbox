package com.brainymobile.android.smsbox.data.entities.templates.template

import com.brainymobile.android.smsbox.data.entities.recipients.toDomainRecipientGroup
import com.brainymobile.android.smsbox.data.entities.recipients.toDomainRecipients
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import com.brainymobile.android.smsbox.domain.entities.template.Template

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
    recipientGroupId = recipientGroup.id,
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