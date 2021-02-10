package com.leviancode.android.gsmbox.ui.recipients.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.EditableRecipientViewModel
import com.leviancode.android.gsmbox.utils.REQUEST_KEY_SAVED
import com.leviancode.android.gsmbox.utils.RESULT_CANCEL
import com.leviancode.android.gsmbox.utils.RESULT_OK
import com.leviancode.android.gsmbox.utils.setNavigationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        showKeyboard()

        binding.viewModel = viewModel
        binding.editTextRecipientName.requestFocus()
        binding.toolbar.apply {
            setNavigationOnClickListener {
                closeDialog(RESULT_CANCEL)
            }
        }

        observeUI()
    }

    private fun observeUI(){
        viewModel.closeDialogLiveEvent.observe(viewLifecycleOwner){ closeDialog(RESULT_OK) }
    }

    private fun showKeyboard() {
        KeyboardUtil.showKeyboard(binding.root)
    }

    private fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(binding.root)
    }

    private fun closeDialog(result: String){
        setNavigationResult(result, REQUEST_KEY_SAVED)
        findNavController().navigateUp()
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        super.onDismiss(dialog)
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}