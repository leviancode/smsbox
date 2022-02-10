package com.brainymobile.android.smsbox.ui.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WidgetListService : RemoteViewsService() {
    @Inject
    lateinit var fetchTemplatesUseCase: FetchTemplatesUseCase

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetListFactory(applicationContext, intent, fetchTemplatesUseCase)
    }
}