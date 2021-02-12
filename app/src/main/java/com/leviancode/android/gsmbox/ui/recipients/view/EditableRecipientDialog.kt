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
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.EditableRecipientViewModel
import com.leviancode.android.gsmbox.utils.*

class EditableRecipientDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogEditableRecipientBinding
    private val viewModel: EditableRecipientViewModel by viewModels()
    private val args: EditableRecipientDialogArgs by navArgs()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_editable_recipient, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.recipient?.let {
            viewModel.setRecipient(it)
            if (it.name.isNotBlank()) binding.toolbar.title = it.name
        }

        binding.viewModel = viewModel
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        showKeyboard(binding.editTextRecipientName)
        observeUI()
    }

    private fun observeUI(){
        viewModel.closeDialogLiveEvent.observe(viewLifecycleOwner){ closeDialog(RESULT_OK) }
    }

    private fun showKeyboard(view: View) {
        KeyboardUtil.showKeyboard(view)
    }

    private fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(requireView())
    }

    private fun closeDialog(result: String){
        hideKeyboard()
        setNavigationResult(result, REQUEST_SAVED)
        findNavController().navigateUp()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}