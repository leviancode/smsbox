package com.leviancode.android.gsmbox.ui.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.core.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.core.utils.log


class WidgetListFactory(val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private var data = mutableListOf<TemplateWithRecipients>()
    private var widgetID = 0

    init {
        widgetID = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    override fun onCreate() {
        log("service create")
        initData()
    }

    override fun onDataSetChanged() {
        log("service data set changed")
        initData()
    }

    override fun onDestroy() {

    }

    private fun initData() {
        data.clear()
        data.addAll(TemplatesRepository.getFavoriteTemplatesSync())
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
        clickIntent.putExtra(FavoritesWidgetProvider.ITEM_ID, getItemId(position).toInt())
        rView.setOnClickFillInIntent(R.id.widget_template_send, clickIntent)

        return rView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = data[position].template.templateId.toLong()

    override fun hasStableIds(): Boolean = true
}