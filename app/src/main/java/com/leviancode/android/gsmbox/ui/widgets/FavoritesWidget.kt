package com.leviancode.android.gsmbox.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.utils.log
import com.leviancode.android.gsmbox.core.utils.managers.SmsManager


/**
 * Implementation of App Widget functionality.
 */
class FavoritesWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_favorites)

        setList(views, context, appWidgetId);
        setListClick(views, context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    private fun setList(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val adapter = Intent(context, WidgetListService::class.java)
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        rv.setRemoteAdapter(R.id.widget_list_view, adapter)
    }

    private fun setListClick(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val listClickIntent = Intent(context, FavoritesWidget::class.java)
        listClickIntent.action = ACTION_ON_CLICK
        val listClickPIntent = PendingIntent.getBroadcast(
            context, 0,
            listClickIntent, 0
        )
        rv.setPendingIntentTemplate(R.id.widget_list_view, listClickPIntent)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action.equals(ACTION_ON_CLICK, ignoreCase = true)) {
            val itemId = intent.getIntExtra(ITEM_ID, -1)
            if (itemId != -1) {
                log("widget item click: $itemId")
                SmsManager.sendSms(context, itemId)
            }
        }
    }

    companion object{
        const val ACTION_ON_CLICK = "com.leviancode.android.gsmbox.itemonclick"
        const val ITEM_ID = "item_id"
    }
}

