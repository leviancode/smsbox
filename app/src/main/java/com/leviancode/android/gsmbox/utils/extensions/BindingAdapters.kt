package com.leviancode.android.gsmbox.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat.setImageTintList
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients

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

@BindingAdapter(value = ["setImageTintGroup"])
fun ImageView.setImageTintGroup(color: String?) {
    if (color.isNullOrBlank()) {
        val tintColor = ContextCompat.getColor(context, R.color.default_group_icon_color)
        setImageTintList(this, ColorStateList.valueOf(tintColor))
    } else {
        setImageTintList(this, ColorStateList.valueOf(Color.parseColor(color)))
    }
}

@BindingAdapter(value = ["setButtonIconTintGroup"])
fun MaterialButton.setButtonIconTintGroup(color: String?) {
    iconTint = if (color.isNullOrBlank()) {
        ContextCompat.getColorStateList(context, R.color.default_group_icon_color)
    } else {
        ColorStateList.valueOf(Color.parseColor(color))
    }
}

@BindingAdapter(value = ["setButtonIconTint"])
fun MaterialButton.setButtonIconTint(color: String?) {
    iconTint = if (color.isNullOrBlank()) {
        ContextCompat.getColorStateList(context, R.color.secondary)
    } else {
        ColorStateList.valueOf(Color.parseColor(color))
    }
}


@BindingAdapter(value = ["setIconTint"])
fun AppCompatImageButton.setIconTint(color: String?) {
    imageTintList = if (isEnabled) {
        if (color.isNullOrBlank()) {
            ContextCompat.getColorStateList(context, R.color.default_send_icon_color)
        } else {
            ColorStateList.valueOf(Color.parseColor(color))
        }
    } else ContextCompat.getColorStateList(context, R.color.ltGrey)
}

@BindingAdapter(value = ["setFavoriteIcon"])
fun ImageButton.setFavoriteIcon(favorite: Boolean) {
    if (favorite) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_24))
    } else {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_outline_24))
    }
}

@BindingAdapter(value = ["setError"])
fun TextInputEditText.showError(isUnique: Boolean) {
    error = if (isUnique) null else context.getString(R.string.err_unique_name)

}

@BindingAdapter(value = ["setTextWithHashtagHighlight"])
fun TextView.setTextWithHashtagHighlight(str: String) {
    text = if (str.contains("#")) {
        SpannableString(str).apply {
            str.findHashtag { tag, startIndex, endIndex ->
                setSpan(
                    ForegroundColorSpan(Color.BLUE), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    StyleSpan(Typeface.ITALIC), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    } else {
        str
    }
}


@BindingAdapter(value = ["setRecipientsAsText"])
fun TextView.setRecipientsAsText(recipients: GroupWithRecipients?) {
    text = when {
        recipients == null -> {
            context.getString(R.string.no_recipients)
        }
        recipients.isGroupNameNotNull() -> {
            "${context.getString(R.string.group)} ${recipients.getRecipientGroupName()} (${recipients.getRecipientsCount()})"
        }
        else -> {
            recipients.getRecipientsAsString()
        }
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
fun ExpandableListView.bindExpandableListAdapter(adapter: BaseExpandableListAdapter) {
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
    visibility =
        if (text.isNullOrBlank() || numberList.contains(text)) View.GONE else View.VISIBLE
}

@BindingAdapter(value = ["setAutoCompleteList"])
fun AutoCompleteTextView.bindAutoCompleteList(list: List<*>?) {
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

