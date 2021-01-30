package com.leviancode.android.gsmbox.ui.view.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

class ColorPickerBottomSheet(
    private val context: Context,
    private val manager: FragmentManager,
    private val defaultColor: Int
) {

    fun show(callback: (Int) -> Unit) {
        MaterialColorPickerDialog.Builder(context)
            .setDefaultColor(defaultColor)
            .setColorSwatch(ColorSwatch._400)
            .setColorShape(ColorShape.CIRCLE)
            .setColorListener { color, _ ->
                callback(color)
            }.showBottomSheet(manager)
    }
}