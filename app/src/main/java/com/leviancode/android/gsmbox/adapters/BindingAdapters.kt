package com.leviancode.android.gsmbox.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.leviancode.android.gsmbox.R

@BindingAdapter(value = ["loadImage"])
fun ImageView.loadImage(uri: String?) {
    if (uri != null && uri.isNotBlank()) {
        load(uri)
    } else {
        load(R.drawable.ic_baseline_library_books_24)
    }
}

@BindingAdapter(value = ["setIcon"])
fun ImageView.bindFoldIcon(isFold: Boolean) {
    if (isFold) {
        load(R.drawable.ic_baseline_arrow_up_24)
    } else {
        load(R.drawable.ic_baseline_arrow_down_24)
    }
}

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: ListAdapter<*, *>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}