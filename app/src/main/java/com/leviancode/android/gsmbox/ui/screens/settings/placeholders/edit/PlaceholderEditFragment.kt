package com.leviancode.android.gsmbox.ui.screens.settings.placeholders.edit

import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentPlaceholderEditBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.utils.extensions.observe
import com.leviancode.android.gsmbox.utils.extensions.navigateBack
import com.leviancode.android.gsmbox.utils.showKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceholderEditFragment : BaseBottomSheet<FragmentPlaceholderEditBinding>(R.layout.fragment_placeholder_edit) {
    private val viewModel: PlaceholderEditViewModel by viewModel()
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
        binding.toolbar.title = if (id == 0) getString(R.string.new_placeholder)
        else getString(R.string.edit_placeholder)
    }
}