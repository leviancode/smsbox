package com.leviancode.android.gsmbox.ui.customviews

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ViewNumberHolderBinding
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.TemplateEditViewModel
import com.leviancode.android.gsmbox.utils.showKeyboard


class RecipientNumbersLayout(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    private val data = mutableListOf<RecipientUI>()

    init {
        orientation = VERTICAL
        layoutTransition = LayoutTransition()
    }

    fun addRecipientView(
        recipient: RecipientUI,
        viewModel: TemplateEditViewModel,
        lifecycleOwner: LifecycleOwner,
        showKeyboard: Boolean = false
    ) {
        data.add(recipient)
        val inflater = LayoutInflater.from(context)
        val recipientBinding = ViewNumberHolderBinding.inflate(
            inflater,
            this,
            true
        )
        recipientBinding.lifecycleOwner = lifecycleOwner
        recipientBinding.viewModel = viewModel
        recipientBinding.recipient = recipient
        recipientBinding.btnRemoveNumber.setOnClickListener {
            data.remove(recipient)
            removeRecipientView(it)
            viewModel.onRemoveRecipientClick(recipient)
        }

        if (childCount > 1) {
            if (showKeyboard) {
                recipientBinding.editTextRecipientNumber.showKeyboard()
            }
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
        }
        recipientBinding.executePendingBindings()
    }

    fun removeRecipientView(view: View) {
        val parent = view.parent as ViewGroup
        val index = indexOfChild(parent)
        if (index > 0) {
            val child = getChildAt(index - 1)
                .findViewById<View>(R.id.edit_text_recipient_number)
            child.showKeyboard()
        }
        removeView(parent)
    }

    fun removeAllRecipientViews() {
        removeAllViews()
    }
}