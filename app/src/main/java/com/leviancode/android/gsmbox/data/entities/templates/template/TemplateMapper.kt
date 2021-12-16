package com.leviancode.android.gsmbox.data.entities.templates.template

import com.leviancode.android.gsmbox.data.entities.recipients.toDomainRecipientGroup
import com.leviancode.android.gsmbox.data.entities.recipients.toDomainRecipients
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.entities.template.Template

fun TemplateWithRecipients.toDomainTemplate() = Template(
    id = template.templateId,
    groupId = template.templateGroupId,
    name = template.name,
    message = template.message,
    iconColor = template.iconColor,
    favorite = template.favorite,
    recipientGroup = recipients?.group?.toDomainRecipientGroup(
        recipients?.recipients?.toDomainRecipients() ?: listOf()
    ) ?: RecipientGroup()
)


fun Template.toTemplateData() = TemplateData(
    templateId = id,
    templateGroupId = groupId,
    name = name,
    message = message,
    iconColor = iconColor,
    favorite = favorite
)

fun Template.toTemplateData(recipientGroupId: Int) = TemplateData(
    templateId = id,
    templateGroupId = groupId,
    recipientGroupId = recipientGroupId,
    name = name,
    message = message,
    iconColor = iconColor,
    favorite = favorite
)

fun List<TemplateWithRecipients>.toDomainTemplates() = map { it.toDomainTemplate() }
fun List<Template>.toDataTemplates() = map { it.toTemplateData() }