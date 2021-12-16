package com.leviancode.android.gsmbox.ui.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import com.leviancode.android.gsmbox.SmsBoxApp
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl.FetchTemplatesUseCaseImpl
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WidgetListService : RemoteViewsService() {
    private val fetchTemplatesUseCase: FetchTemplatesUseCase by inject<FetchTemplatesUseCaseImpl>()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetListFactory(applicationContext, intent, fetchTemplatesUseCase)
    }
}