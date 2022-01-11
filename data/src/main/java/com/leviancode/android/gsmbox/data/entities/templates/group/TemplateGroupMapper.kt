package com.leviancode.android.gsmbox.data.entities.templates.group

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup

fun GroupWithTemplates.toDomainGroup() = TemplateGroup(
    id = group.templateGroupId,
    position = group.position,
    name = group.name,
    description = group.description,
    iconColor = group.iconColor,
    size = templates.size,
    timestamp = group.timestamp
)

fun TemplateGroup.toDataGroup() = TemplateGroupData(
    templateGroupId = id,
    position = position,
    name = this.name,
    description = this.description,
    iconColor = this.iconColor,
    timestamp = timestamp
)

fun List<TemplateGroup>.toDataTemplateGroups() = map { it.toDataGroup() }
fun List<GroupWithTemplates>.toDomainGroups() = map { it.toDomainGroup() }