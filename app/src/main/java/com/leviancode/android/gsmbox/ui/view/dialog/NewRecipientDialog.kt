package com.leviancode.android.gsmbox.ui.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewRecipientBinding
import com.leviancode.android.gsmbox.ui.viewmodel.NewRecipientDialogViewModel
import com.leviancode.android.gsmbox.utils.REQUEST_KEY_SAVED
import com.leviancode.android.gsmbox.utils.RESULT_CANCEL
import com.leviancode.android.gsmbox.utils.RESULT_OK
import com.leviancode.android.gsmbox.utils.setNavigationResult

class NewRecipientDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogNewRecipientBinding
    private val viewModel: NewRecipientDialogViewModel by viewModels()
    private val args: NewRecipientDialogArgs by navArgs()
    private var editMode = false

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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_recipient, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
        viewModel.loadRecipientById(args.recipientId)

        binding.viewModel = viewModel
        binding.editTextRecipientName.requestFocus()
        binding.toolbar.apply {
            setNavigationOnClickListener {
                removeRecipient()
                closeDialog(RESULT_CANCEL)
            }
            setOnMenuItemClickListener {
                saveRecipient()
                closeDialog(RESULT_OK)
                true
            }
        }

        observeUI()
    }

    private fun observeUI(){
        val btnSave = binding.toolbar.menu.findItem(R.id.menu_save)
        viewModel.fieldsNotEmptyLiveEvent.observe(viewLifecycleOwner){
            btnSave.isEnabled = it
        }
    }

    private fun saveRecipient(){
        viewModel.saveRecipient()
    }

    private fun removeRecipient(){
        viewModel.removeRecipient()
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