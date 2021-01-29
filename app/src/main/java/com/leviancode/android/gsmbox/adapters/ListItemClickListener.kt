package com.leviancode.android.gsmbox.adapters

import android.view.View

class ListItemClickListener<T>(val clickListener: (item: T) -> Unit) {
    fun onClick(item: T) = clickListener(item)
}