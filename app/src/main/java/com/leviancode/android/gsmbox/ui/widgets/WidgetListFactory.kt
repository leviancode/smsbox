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
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateUI
import com.leviancode.android.gsmbox.ui.entities.templates.toUITemplates
import com.leviancode.android.gsmbox.utils.logI


class WidgetListFactory(
    val context: Context,
    intent: Intent,
    private val fetchTemplatesUseCase: FetchTemplatesUseCase
) : RemoteViewsService.RemoteViewsFactory {
    private var data = mutableListOf<TemplateUI>()
    private var widgetID = 0

    init {
        widgetID = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    override fun onCreate() {
        logI("service create")
        initData()
    }

    override fun onDataSetChanged() {
        logI("service data set changed")
        initData()
    }

    override fun onDestroy() {}

    private fun initData() {
        val favorites = fetchTemplatesUseCase.getFavoritesSync().toUITemplates()
        data.clear()
        data.addAll(favorites)
    }

    override fun getCount(): Int = data.size

    override fun getViewAt(position: Int): RemoteViews {
        val item = data[position]

        val rView = RemoteViews(
            context.packageName,
            R.layout.widget_item
        ).apply {
            setTextViewText(R.id.widget_template_name, item.getName())
            setTextViewText(R.id.widget_template_message, item.getMessage())
            setTextViewText(
                R.id.widget_template_recipient, item.recipientGroup.getFormatRecipients(context)
            )
        }

        ContextCompat.getDrawable(context, R.drawable.ic_baseline_send_24)?.let { icon ->
            icon.setTint(Color.parseColor(item.getIconColor()))
            rView.setImageViewBitmap(R.id.widget_template_send, icon.toBitmap())
        }

        val clickIntent = Intent().apply {
            putExtra(FavoritesWidgetProvider.ITEM_ID, getItemId(position).toInt())
        }
        rView.setOnClickFillInIntent(R.id.widget_template_send, clickIntent)

        return rView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = data[position].id.toLong()

    override fun hasStableIds(): Boolean = true
}