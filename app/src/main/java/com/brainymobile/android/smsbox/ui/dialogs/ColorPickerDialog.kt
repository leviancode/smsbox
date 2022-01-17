package com.brainymobile.android.smsbox.ui.dialogs

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.brainymobile.android.smsbox.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape

class ColorPickerDialog(
    private val context: Context,
    private val manager: FragmentManager,
    private val defaultColor: String
) {

    fun show(callback: (String) -> Unit) {
        val colors = context.resources.getStringArray(R.array.colors_400)
        MaterialColorPickerDialog.Builder(context)
            .setDefaultColor(defaultColor)
            .setColors(colors)
            .setTickColorPerCard(true)
            .setColorShape(ColorShape.CIRCLE)
            .setColorListener { _, color ->
                callback(color)
            }.showBottomSheet(manager)
    }
}