package com.brainymobile.android.smsbox.ui.screens.templates.templates.edit.adapters

import androidx.core.view.isVisible
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.ViewNumberHolderBinding
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientUI
import com.brainymobile.android.smsbox.ui.screens.templates.templates.edit.TemplateEditViewModel
import com.brainymobile.android.smsbox.utils.showKeyboard

private var firstLoad: Boolean = true
private var size: Int = 0

class RecipientListAdapter(private val viewModel: TemplateEditViewModel) :
    BaseListAdapter<RecipientUI, ViewNumberHolderBinding>(
        R.layout.view_number_holder,
        bind = { binding, item, position ->
            binding.btnRemoveNumber.isVisible = position != 0
            binding.recipient = item
            binding.viewModel = viewModel

            if (!firstLoad && position == size - 1){
                binding.editTextRecipientNumber.showKeyboard()
            }
        }){

    override var onSubmitComplete = {
        size = itemCount
        firstLoad = false
    }
}