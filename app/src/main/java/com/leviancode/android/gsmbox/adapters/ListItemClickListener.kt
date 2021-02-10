package com.leviancode.android.gsmbox.adapters

class ListItemClickListener<T>(val clickListener: (item: T) -> Unit) {
    fun onClick(item: T) = clickListener(item)
}