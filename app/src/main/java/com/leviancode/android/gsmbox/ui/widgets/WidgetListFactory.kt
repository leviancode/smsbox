package com.leviancode.android.gsmbox.ui.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.core.data.repository.AppDatabase
import com.leviancode.android.gsmbox.core.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.core.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WidgetListFactory(val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private var data = mutableListOf<TemplateWithRecipients>()
    private var widgetID = 0
    private var liveData = TemplatesRepository.getFavoriteTemplatesLiveData()
    private val observer = Observer<List<TemplateWithRecipients>>{
        data.clear()
        data.addAll(it)
    }

    init {
        AppDatabase.init(context)
        widgetID = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    override fun onCreate() {
        liveData.observeForever(observer)
    }

    override fun onDataSetChanged() {
        log("widget data set changed")
    }

    override fun onDestroy() {
        CoroutineScope(Dispatchers.Main).launch {
            liveData.removeObserver(observer)
        }
    }

    override fun getCount(): Int = data.size

    override fun getViewAt(position: Int): RemoteViews {
        val rView = RemoteViews(
            context.packageName,
            R.layout.widget_item
        )
        val item = data[position]
        rView.setTextViewText(R.id.widget_template_name, item.template.getName())
        rView.setTextViewText(R.id.widget_template_message, item.template.getMessage())
        rView.setTextViewText(
            R.id.widget_template_recipient, item.recipients?.getFormatRecipients(
                context
            )
        )
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_send_24)?.let { icon ->
            icon.setTint(Color.parseColor(item.template.getIconColor()))
            rView.setImageViewBitmap(R.id.widget_template_send, icon.toBitmap())
        }

        val clickIntent = Intent()
        clickIntent.flags
        clickIntent.putExtra(FavoritesWidget.ITEM_ID, getItemId(position).toInt())
        rView.setOnClickFillInIntent(R.id.widget_template_send, clickIntent)

        return rView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = data[position].template.templateId.toLong()

    override fun hasStableIds(): Boolean = true
}