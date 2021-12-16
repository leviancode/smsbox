package com.leviancode.android.gsmbox.data.entities.templates.group

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup

fun GroupWithTemplates.toDomainGroup() = TemplateGroup(
    id = group.templateGroupId,
    name = group.name,
    description = group.description,
    iconColor = group.iconColor,
    size = templates.size
)

fun TemplateGroup.toDataGroup() = TemplateGroupData(
    templateGroupId = id,
    name = this.name,
    description = this.description,
    iconColor = this.iconColor
)

fun List<TemplateGroup>.toDataTemplateGroups() = map { it.toDataGroup() }
fun List<GroupWithTemplates>.toDomainGroups() = map { it.toDomainGroup() }