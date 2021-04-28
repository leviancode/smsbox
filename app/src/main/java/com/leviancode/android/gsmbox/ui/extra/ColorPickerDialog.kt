package com.leviancode.android.gsmbox.ui.extra

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.leviancode.android.gsmbox.R

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