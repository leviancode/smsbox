package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DiscardAlertDialog
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.goBack

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
                goBack()
            }
            RESULT_CANCEL -> {
                if (isDataEdited()) {
                    showDiscardDialog()
                } else {
                    goBack()
                }
            }
        }
    }

    fun showDiscardDialog() {
        DiscardAlertDialog.show(requireContext()) { response ->
            if (response) {
                goBack()
            }
        }
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}