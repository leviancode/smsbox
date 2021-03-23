package com.leviancode.android.gsmbox.data.model.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Ignore
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
    fun getSize() = templates.size
}
