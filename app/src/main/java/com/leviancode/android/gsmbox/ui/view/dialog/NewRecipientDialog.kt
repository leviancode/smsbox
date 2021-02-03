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
import androidx.navigation.fragment.navArgs
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewRecipientBinding
import com.leviancode.android.gsmbox.ui.viewmodel.RecipientViewModel

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
            title = getString(R.string.title_new_recipient)
            setNavigationOnClickListener { v: View? ->
                viewModel.removeRecipient()
                dismiss()
            }
            setOnMenuItemClickListener { item ->
                viewModel.saveRecipient()
                showToast(getString(R.string.recipient_saved))
                dismiss()
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

    fun showKeyboard() {
        KeyboardUtil.showKeyboard(binding.root)
        /*val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)*/
    }

    fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(binding.root)
        /*val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)*/
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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