package com.leviancode.android.gsmbox.adapters

import android.graphics.Color
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.leviancode.android.gsmbox.R

@BindingAdapter(value = ["loadImage"])
fun ImageView.loadImage(uri: String?) {
    if (uri.isNullOrBlank()) {
        load(R.drawable.ic_baseline_library_books_24)
    } else {
        try {
            load(uri)
        } catch (e: Exception) {
            load(R.drawable.ic_baseline_library_books_24)
        }
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

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}

