package com.brainymobile.android.smsbox.ui.screens.recipients.groups.edit

import android.content.DialogInterface
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentRecipientGroupEditBinding
import com.brainymobile.android.smsbox.ui.base.BaseBottomSheet
import com.brainymobile.android.smsbox.ui.dialogs.ColorPickerDialog
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipientGroupEditDialog private constructor(
    private val groupId: Int,
    private val onGroupEdit: (groupId: Int) -> Unit,
    private val onDialogDismiss: () -> Unit = {},
) : BaseBottomSheet<FragmentRecipientGroupEditBinding>(R.layout.fragment_recipient_group_edit) {
    private val viewModel: RecipientGroupEditViewModel by viewModels()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override val showKeyboardOnStarted: Boolean = true

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss()
    }

    override fun onCreated() {
        ViewCompat.setElevation(binding.root, 8.0f)
        binding.viewModel = viewModel
        setTitle(groupId != 0)
        loadData()
        observeEvents()
    }

    private fun loadData() {
        viewModel.loadGroup(groupId).observe(viewLifecycleOwner) { group ->
            binding.model = group
            binding.executePendingBindings()
        }
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_group)
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { closeDialog(null) }

        viewModel.closeDialogEvent.observe(viewLifecycleOwner) { groupId ->
            closeDialog(groupId)
        }

        viewModel.selectColorEvent.observe(viewLifecycleOwner) { selectColor(it) }
    }

    private fun selectColor(group: RecipientGroupUI) {
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            group.getIconColor()
        ).show {
            group.setIconColor(it)
        }
    }

    private fun closeDialog(groupId: Int?) {
        groupId?.let { onGroupEdit(groupId) }
        close()
    }

    companion object {
        fun show(
            fm: FragmentManager,
            groupId: Int = 0,
            onGroupEdit: (Int) -> Unit = {},
            onDialogDismiss: () -> Unit = {}
        ) {
            RecipientGroupEditDialog(groupId, onGroupEdit, onDialogDismiss).show(
                fm,
                null
            )
        }
    }
}