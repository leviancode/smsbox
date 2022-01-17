package com.brainymobile.android.smsbox.ui.dialogs

import android.view.View
import androidx.core.content.ContextCompat.getColor
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.*
import com.github.zawadz88.materialpopupmenu.popupMenu

class PopupMenus(val view: View) {
    private val context get() = view.context

    fun showEditDelete(callback: (MenuItem) -> Unit) = popupMenu {
        style = R.style.Widget_MPM_Menu_Dark_CustomBackground
        section {
            item {
                label = context.getString(R.string.edit)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_outline_edit_24
                this.callback = {
                    callback(EDIT)
                }
            }
            item {
                label = context.getString(R.string.delete)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_baseline_delete_forever_24
                this.callback = {
                    callback(DELETE)
                }
            }
        }
    }.show(context, view)

    fun showEditAddToGroupDelete(callback: (MenuItem) -> Unit) = popupMenu {
        style = R.style.Widget_MPM_Menu_Dark_CustomBackground
        section {
            item {
                label = context.getString(R.string.edit)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_outline_edit_24
                this.callback = {
                    callback(EDIT)
                }
            }
            item {
                label = context.getString(R.string.add_to_group)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_baseline_list_add_24
                this.callback = {
                    callback(ADD)
                }
            }
            item {
                label = context.getString(R.string.delete)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_baseline_delete_forever_24
                this.callback = {
                    callback(DELETE)
                }
            }
        }
    }.show(context, view)

    fun showEditRemoveDelete(callback: (MenuItem) -> Unit) = popupMenu {
        style = R.style.Widget_MPM_Menu_Dark_CustomBackground
        section {
            item {
                label = context.getString(R.string.edit)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_outline_edit_24
                this.callback = {
                    callback(EDIT)
                }
            }
            item {
                label = context.getString(R.string.remove_from_group)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.playlist_remove
                this.callback = {
                    callback(REMOVE)
                }
            }
            item {
                label = context.getString(R.string.delete)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_baseline_delete_forever_24
                this.callback = {
                    callback(DELETE)
                }
            }
        }
    }.show(context, view)

    fun showEditAddRecipientClearDelete(callback: (MenuItem) -> Unit) = popupMenu {
        style = R.style.Widget_MPM_Menu_Dark_CustomBackground
        section {
            item {
                label = context.getString(R.string.edit)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_outline_edit_24
                this.callback = {
                    callback(EDIT)
                }
            }

            item {
                label = context.getString(R.string.add_recipient)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_baseline_list_add_24
                this.callback = {
                    callback(ADD)
                }
            }

            item {
                label = context.getString(R.string.clear_group)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.playlist_remove
                this.callback = {
                    callback(CLEAR)
                }
            }
            item {
                label = context.getString(R.string.delete)
                labelColor = getColor(context, R.color.primaryTextColor)
                iconColor = getColor(context, R.color.popup_menu)
                icon = R.drawable.ic_baseline_delete_forever_24
                this.callback = {
                    callback(DELETE)
                }
            }
        }
    }.show(context, view)

    enum class MenuItem {
        ADD,
        EDIT,
        REMOVE,
        DELETE,
        CLEAR
    }
}