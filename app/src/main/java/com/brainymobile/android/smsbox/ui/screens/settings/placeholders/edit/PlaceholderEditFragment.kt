package com.brainymobile.android.smsbox.ui.screens.settings.placeholders.edit

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentPlaceholderEditBinding
import com.brainymobile.android.smsbox.ui.base.BaseBottomSheet
import com.brainymobile.android.smsbox.utils.extensions.navigateBack
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceholderEditFragment :
    BaseBottomSheet<FragmentPlaceholderEditBinding>(R.layout.fragment_placeholder_edit) {
    private val viewModel: PlaceholderEditViewModel by viewModels()
    private val args: PlaceholderEditFragmentArgs by navArgs()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override fun onCreated() {
        binding.viewModel = viewModel
        setTitle(args.id)
        showKeyboard()
        loadData()
        observeEvents()
    }

    private fun loadData() {
        viewModel.load(args.id).observe(this) { placeholder ->
            binding.model = placeholder
            binding.executePendingBindings()
        }
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { navigateBack() }
        viewModel.placeholderSavedEvent.observe(viewLifecycleOwner) {
            navigateBack()
        }
    }

    private fun setTitle(id: Int) {
        binding.toolbar.title = if (id == 0) getString(R.string.new_variable)
        else getString(R.string.edit_variable)
    }
}