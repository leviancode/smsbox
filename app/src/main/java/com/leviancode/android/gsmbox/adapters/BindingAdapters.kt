package com.leviancode.android.gsmbox.adapters

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textfield.TextInputEditText
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.templates.Template

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

@BindingAdapter(value = ["setError"])
fun TextInputEditText.showError(isUnique: Boolean) {
    error = if (isUnique) null else context.getString(R.string.err_unique_name)
}

@BindingAdapter(value = ["setRecipientsAsText"])
fun TextView.setRecipientsAsText(template: Template) {
    text = if (template.getRecipientGroup() != null) {
        context.getString(R.string.recipient_group) + ": " + template.getRecipientGroupName()
    } else {
       template.getRecipientsAsString()
    }
}

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

@BindingAdapter(value = ["visibleIfTextNotEmpty", "visibleIfNumberNotSaved"], requireAll = true)
fun View.setVisibility(text: String?, numberList: List<String>?) {
    if (numberList == null) return
    visibility = if (text.isNullOrBlank() || numberList.contains(text)) View.GONE else View.VISIBLE
}

@BindingAdapter(value = ["setAutoCompleteList"])
fun AutoCompleteTextView.bindAutoCompleteList(list: List<*>?){
    list?.let {
        setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
        )
    }
}

