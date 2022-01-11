package com.leviancode.android.gsmbox.data.entities.templates.group

import androidx.room.Embedded
import androidx.room.Relation
import com.leviancode.android.gsmbox.data.entities.templates.template.TemplateData

data class GroupWithTemplates(
    @Embedded val group: TemplateGroupData,
    @Relation(
        parentColumn = "templateGroupId",
        entityColumn = "templateGroupId",
    )
    val templates: List<TemplateData>
)
