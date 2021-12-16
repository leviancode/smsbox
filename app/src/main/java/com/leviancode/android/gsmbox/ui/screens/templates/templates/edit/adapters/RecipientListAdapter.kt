package com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.adapters

import androidx.core.view.isVisible
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ViewNumberHolderBinding
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.TemplateEditViewModel
import com.leviancode.android.gsmbox.utils.showKeyboard

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