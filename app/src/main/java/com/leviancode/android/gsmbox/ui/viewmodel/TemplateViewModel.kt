package com.leviancode.android.gsmbox.ui.viewmodel

import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository

class TemplateViewModel {
    private val repository = TemplatesRepository
    val template = TemplateObservable()

    fun setData(data: Template){
        template.data = data
    }

    fun onFavoriteClick(){
        template.setFavorite(!template.isFavorite())
        repository.updateTemplate(template.data)
    }
}