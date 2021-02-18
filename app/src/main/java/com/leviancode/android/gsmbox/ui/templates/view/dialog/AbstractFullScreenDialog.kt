package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.extra.DiscardDialog
import com.leviancode.android.gsmbox.utils.KeyboardUtil
import com.leviancode.android.gsmbox.utils.RESULT_CANCEL
import com.leviancode.android.gsmbox.utils.RESULT_OK
import com.leviancode.android.gsmbox.utils.showToast

abstract class AbstractFullScreenDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), R.style.AppTheme_FullScreenDialog){
            override fun onBackPressed() {
                closeDialog(RESULT_CANCEL)
            }
        }
    }

    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    abstract fun isDataEdited(): Boolean

    fun closeDialog(result: String){
        hideKeyboard()
        when (result) {
            RESULT_OK -> {
                showToast(requireContext(), getString(R.string.saved))
                findNavController().navigateUp()
            }
            RESULT_CANCEL -> {
                if (isDataEdited()) {
                    showDiscardDialog()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
    }

    fun showDiscardDialog() {
        DiscardDialog(requireContext()).show { response ->
            if (response) {
                findNavController().navigateUp()
            }
        }
    }

    fun showKeyboard(view: View) {
        KeyboardUtil.showKeyboard(view)
    }

    fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(requireView())
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