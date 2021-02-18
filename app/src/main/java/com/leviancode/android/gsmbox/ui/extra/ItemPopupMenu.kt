package com.leviancode.android.gsmbox.ui.extra

import android.content.Context
import android.view.View
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.github.zawadz88.materialpopupmenu.popupMenuBuilder
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.utils.ADD
import com.leviancode.android.gsmbox.utils.DELETE
import com.leviancode.android.gsmbox.utils.EDIT

class ItemPopupMenu(val context: Context, val view: View) {

    fun showSimple(callback: (String) -> Unit) {
        popupMenu {
            section {
                item {
                    label = context.getString(R.string.edit)
                    icon = R.drawable.ic_outline_edit_24
                    this.callback = {
                        callback(EDIT)
                    }
                }
                item {
                    label = context.getString(R.string.delete)
                    icon = R.drawable.ic_baseline_delete_forever_24
                    this.callback = {
                        callback(DELETE)
                    }
                }
            }
        }.show(context, view)
    }

    fun showForRecipientGroup(callback: (String) -> Unit){
        popupMenu {
            section {
                item {
                    label = context.getString(R.string.add_recipient)
                    icon = R.drawable.ic_baseline_library_add_24
                    this.callback = {
                        callback(ADD)
                    }
                }
                item {
                    label = context.getString(R.string.edit)
                    icon = R.drawable.ic_outline_edit_24
                    this.callback = {
                        callback(EDIT)
                    }
                }
                item {
                    label = context.getString(R.string.delete)
                    icon = R.drawable.ic_baseline_delete_forever_24
                    this.callback = {
                        callback(DELETE)
                    }
                }
            }
        }.show(context, view)
    }
}