package com.leviancode.android.gsmbox.adapters

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient

@BindingAdapter(value = ["loadImage"])
fun ImageView.loadImage(uri: String?) {
    if (uri.isNullOrBlank()) {
        load(R.drawable.ic_baseline_source_24)
    } else {
        try {
            load(uri)
        } catch (e: Exception) {
            load(R.drawable.ic_baseline_source_24)
        }
    }
}

/*@BindingAdapter(value = ["setIcon"])
fun ImageView.bindFoldIcon(isFold: Boolean) {
    if (isFold) {
        load(R.drawable.ic_baseline_arrow_up_24)
    } else {
        load(R.drawable.ic_baseline_arrow_down_24)
    }
}*/

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: ListAdapter<*, *>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["setExpandableAdapter"])
fun ExpandableListView.bindExpandableListAdapter(adapter: BaseExpandableListAdapter){
    setAdapter(adapter)
}

@BindingAdapter(value = ["visibilityDependingOnText"])
fun View.setVisibility(text: String?) {
    this.run {
        visibility = if (text.isNullOrBlank()) View.GONE else View.VISIBLE
    }
}

@BindingAdapter(value = ["setAutoCompleteList"])
fun AutoCompleteTextView.setAdapter(recipients: List<Recipient>?){
    recipients?.let {
        setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                recipients.map { it.phoneNumber }
            )
        )
    }
}

