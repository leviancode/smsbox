package com.leviancode.android.gsmbox.ui.screens.recipients.groups.edit

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.dialogs.ColorPickerDialog
import com.leviancode.android.gsmbox.utils.REQ_CREATE_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard
import com.leviancode.android.gsmbox.databinding.FragmentRecipientGroupEditBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientGroupEditFragment : BaseBottomSheet<FragmentRecipientGroupEditBinding>(R.layout.fragment_recipient_group_edit)  {
    private val viewModel: RecipientGroupEditViewModel by viewModel()
    private val args: RecipientGroupEditFragmentArgs by navArgs()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override fun onCreated() {
        binding.viewModel = viewModel
        setTitle(args.groupId != 0)
        loadData()
        observeEvents()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val group = viewModel.loadGroup(args.groupId)
            binding.model = group
            showKeyboard()
        }
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_group)
    }

    private fun observeEvents(){
        binding.toolbar.setNavigationOnClickListener { closeDialog(null) }

        viewModel.closeDialogEvent.observe(viewLifecycleOwner){ group ->
            closeDialog(group)
        }

        viewModel.selectColorEvent.observe(viewLifecycleOwner){ selectColor(it) }
    }

    private fun selectColor(group: RecipientGroupUI){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            group.getIconColor()
        ).show {
            group.setIconColor(it)
        }
    }

    private fun closeDialog(group: RecipientGroupUI?) {
        setNavigationResult(group, REQ_CREATE_RECIPIENT_GROUP)
        close()
    }
}