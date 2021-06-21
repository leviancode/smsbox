package com.leviancode.android.gsmbox.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.data.repository.AppDatabase
import com.leviancode.android.gsmbox.core.utils.log
import com.leviancode.android.gsmbox.core.utils.managers.SmsManager


class FavoritesWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        log("widget update")
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

        setList(views, context, appWidgetId)
        setListClick(views, context, appWidgetId)
        setUpdateClick(views, context, appWidgetId)

        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        AppDatabase.init(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        log("widget disabled")
    }

    private fun setList(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val adapter = Intent(context, WidgetListService::class.java)
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        rv.setRemoteAdapter(R.id.widget_list_view, adapter)
    }

    private fun setListClick(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val listClickIntent = Intent(context, FavoritesWidgetProvider::class.java)
        listClickIntent.action = ACTION_ON_CLICK
        val listClickPIntent = PendingIntent.getBroadcast(
            context, 0,
            listClickIntent, 0
        )
        rv.setPendingIntentTemplate(R.id.widget_list_view, listClickPIntent)
    }

    private fun setUpdateClick(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val updIntent = Intent(context, FavoritesWidgetProvider::class.java)
        updIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        val updPIntent = PendingIntent.getBroadcast(
            context,
            appWidgetId, updIntent, 0
        )
        rv.setOnClickPendingIntent(R.id.widget_button_update, updPIntent)
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
