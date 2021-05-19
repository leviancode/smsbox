package com.leviancode.android.gsmbox.ui.fragments.templates.groups.edit

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.dialogs.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.dialogs.AbstractFullScreenEditableDialog
import com.leviancode.android.gsmbox.core.utils.RESULT_CANCEL
import com.leviancode.android.gsmbox.core.utils.RESULT_OK
import com.leviancode.android.gsmbox.core.utils.hideKeyboard
import com.leviancode.android.gsmbox.core.utils.showKeyboard

class EditableTemplateGroupDialog : AbstractFullScreenEditableDialog<DialogEditableTemplateGroupBinding>(R.layout.dialog_editable_template_group) {
    private lateinit var binding: DialogEditableTemplateGroupBinding
    private val viewModel: EditableTemplateGroupViewModel by viewModels()
    private val args: EditableTemplateGroupDialogArgs by navArgs()

    override fun setupViews(binding: DialogEditableTemplateGroupBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        setTitle(args.groupId != 0)
        showKeyboard(binding.editTextTemplateGroupName)
        fetchData()
        observeEvents()
    }

    private fun fetchData() {
        viewModel.loadGroup(args.groupId).observe(viewLifecycleOwner){
            binding.model = it
            binding.executePendingBindings()
        }
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_group)
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.chooseColorLiveEvent.observe(viewLifecycleOwner) { selectColor(it) }

        viewModel.savedLiveEvent.observe(viewLifecycleOwner) { closeDialog(RESULT_OK) }
    }

    private fun selectColor(color: String) {
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    override fun isDataEdited(): Boolean {
        return viewModel.isGroupEdited()
    }
}