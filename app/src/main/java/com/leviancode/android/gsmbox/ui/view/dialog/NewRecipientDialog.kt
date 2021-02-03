package com.leviancode.android.gsmbox.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewRecipientBinding
import com.leviancode.android.gsmbox.ui.viewmodel.RecipientViewModel
import com.leviancode.android.gsmbox.utils.setNavigationResult

class NewRecipientDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogNewRecipientBinding
    private val viewModel: RecipientViewModel by viewModels()
    private val args: NewRecipientDialogArgs by navArgs()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

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
            setNavigationOnClickListener { v: View? ->
                removeRecipient()
                closeDialog(false)
            }
            setOnMenuItemClickListener { item ->
                saveRecipient()
                closeDialog(true)
                true
            }
        }

        observe()
    }

    fun observe(){
        val btnSave = binding.toolbar.menu.findItem(R.id.menu_save)
        viewModel.fieldsNotEmptyLiveEvent.observe(viewLifecycleOwner){
            btnSave.isEnabled = it
        }
    }

    fun saveRecipient(){
        viewModel.saveRecipient()
    }

    fun removeRecipient(){
        viewModel.removeRecipient()
    }

    fun showKeyboard() {
        KeyboardUtil.showKeyboard(binding.root)
    }

    fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(binding.root)
    }

    private fun closeDialog(result: Boolean){
        setNavigationResult(result, SAVED_REQUEST_KEY)
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        super.onDismiss(dialog)
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    companion object{
        val TAG = NewRecipientDialog::class.java.simpleName
        const val SAVED_REQUEST_KEY = "isSaved"
    }
}