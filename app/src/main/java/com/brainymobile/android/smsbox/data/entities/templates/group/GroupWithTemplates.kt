package com.brainymobile.android.smsbox.data.entities.templates.group

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Relation
import com.brainymobile.android.smsbox.data.entities.templates.template.TemplateData

data class GroupWithTemplates(
    @Embedded val group: TemplateGroupData,
    @Relation(
        parentColumn = "templateGroupId",
        entityColumn = "templateGroupId",
    )
    val templates: List<TemplateData>
) : BaseObservable() {

    @Bindable
    fun getSize() = templates.size.toString()

    fun copy() = GroupWithTemplates(
        group = this.group.copy(),
        templates = this.templates.map { it.copy() }
    )
}
