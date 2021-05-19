package com.leviancode.android.gsmbox.core.data.model.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithTemplates(
    @Embedded val group: TemplateGroup,
    @Relation(
        parentColumn = "templateGroupId",
        entityColumn = "templateGroupId",
    )
    val templates: List<Template>
) : BaseObservable() {

    @Bindable
    fun getSize() = templates.size.toString()

    fun copy() = GroupWithTemplates(
        group = this.group.copy(),
        templates = this.templates.map { it.copy() }
    )
}
