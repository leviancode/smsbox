package com.brainymobile.android.smsbox.ui.widgets

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.utils.logI
import com.brainymobile.android.smsbox.utils.managers.SmsManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class FavoritesWidgetProvider : AppWidgetProvider() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SmsManagerEntryPoint {
        fun smsManager(): SmsManager
    }

    private fun getSmsManager(context: Context): SmsManager {
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(context, SmsManagerEntryPoint::class.java)
        return hiltEntryPoint.smsManager()
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        logI("widget update")
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

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        logI("widget disabled")
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
            listClickIntent, FLAG_MUTABLE
        )
        rv.setPendingIntentTemplate(R.id.widget_list_view, listClickPIntent)
    }

    private fun setUpdateClick(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val updIntent = Intent(context, FavoritesWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        }

        val updPIntent = PendingIntent.getBroadcast(
            context,
            appWidgetId, updIntent, FLAG_MUTABLE
        )
        rv.setOnClickPendingIntent(R.id.widget_button_update, updPIntent)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action.equals(ACTION_ON_CLICK, ignoreCase = true)) {
            val itemId = intent.getIntExtra(ITEM_ID, -1)
            if (itemId != -1) {
                getSmsManager(context.applicationContext).sendSms(itemId)
            }
        }
    }

    companion object {
        const val ACTION_ON_CLICK = "com.brainymobile.android.smsbox.itemonclick"
        const val ITEM_ID = "item_id"
    }
}

