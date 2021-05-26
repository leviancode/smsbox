package com.leviancode.android.gsmbox.ui.widgets

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetListService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetListFactory(applicationContext, intent)
    }
}