package com.brainymobile.android.smsbox.ui.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.FetchTemplatesUseCaseImpl
import org.koin.android.ext.android.inject

class WidgetListService : RemoteViewsService() {
    private val fetchTemplatesUseCase: FetchTemplatesUseCase by inject<FetchTemplatesUseCaseImpl>()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetListFactory(applicationContext, intent, fetchTemplatesUseCase)
    }
}