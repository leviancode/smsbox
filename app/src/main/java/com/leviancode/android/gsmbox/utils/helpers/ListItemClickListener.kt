package com.leviancode.android.gsmbox.utils.helpers

class ListItemClickListener<T>(val clickListener: (item: T) -> Unit) {
    fun onClick(item: T) = clickListener(item)
}