package com.leviancode.android.gsmbox.ui.viewmodel

import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TemplateViewModel {
    private val repository = TemplatesRepository
    val template = TemplateObservable()

    fun setData(data: Template){
        template.data = data
    }

    fun onFavoriteClick(){
        template.setFavorite(!template.isFavorite())
        CoroutineScope(Dispatchers.Main).launch {
            repository.updateTemplate(template.data)
        }
    }

    
}