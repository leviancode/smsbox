package com.leviancode.android.gsmbox.ui.screens.recipients.groups.edit

import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentRecipientGroupEditBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.dialogs.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.select.RecipientGroupMultiSelectListDialog
import com.leviancode.android.gsmbox.utils.REQ_CREATE_RECIPIENT_GROUP_ID
import com.leviancode.android.gsmbox.utils.extensions.observe
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResultLiveData
import com.leviancode.android.gsmbox.utils.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientGroupEditDialog private constructor(
    private val groupId: Int,
    private val callback: (Int) -> Unit
) : BaseBottomSheet<FragmentRecipientGroupEditBinding>(R.layout.fragment_recipient_group_edit) {
    private val viewModel: RecipientGroupEditViewModel by viewModel()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override val showKeyboardOnStarted: Boolean = true

    override fun onCreated() {
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
        groupId?.let { callback(groupId) }
        close()
    }

    companion object {
        fun show(
            fm: FragmentManager,
            groupId: Int = 0,
            callback: (Int) -> Unit = {}
        ) {
            RecipientGroupEditDialog(groupId, callback).show(
                fm,
                null
            )
        }
    }
}