package com.leviancode.android.gsmbox.ui.recipients.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.EditableRecipientGroupViewModel
import com.leviancode.android.gsmbox.ui.templates.view.dialog.ColorPickerDialog
import com.leviancode.android.gsmbox.utils.KeyboardUtil

class EditableRecipientGroupDialog : BottomSheetDialogFragment()  {
    private lateinit var binding: DialogEditableRecipientGroupBinding
    private val viewModel: EditableRecipientGroupViewModel by viewModels()
    private val args: EditableRecipientGroupDialogArgs by navArgs()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_editable_recipient_group, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        viewModel.setGroup(args.group)
        setTitle()
        showKeyboard(binding.editTextRecipientGroupName)
        observeUI()
    }

    private fun setTitle(){
        if (args.group.name.isNotBlank()) binding.toolbar.title = args.group.name
    }

    private fun observeUI(){
        binding.toolbar.setNavigationOnClickListener { closeDialog() }

        viewModel.closeDialogLiveEvent.observe(viewLifecycleOwner){
            closeDialog()
        }

        viewModel.chooseColorLiveEvent.observe(viewLifecycleOwner){ selectColor(it) }
    }

    private fun selectColor(color: Int){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    private fun showKeyboard(view: View) {
        KeyboardUtil.showKeyboard(view)
    }

    private fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(requireView())
    }

    private fun closeDialog(){
        hideKeyboard()
        findNavController().navigateUp()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}