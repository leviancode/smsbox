package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.extra.DiscardDialog
import com.leviancode.android.gsmbox.utils.KeyboardUtil

abstract class AbstractFullScreenDialog : DialogFragment() {
    var discarded = false
    var saved = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme){
            override fun onBackPressed() {
                closeDialog()
            }
        }
    }

    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            view?.visibility = View.VISIBLE
            window?.setLayout(width, height)
           // window?.setWindowAnimations(R.style.Theme_GsmBox_Slide)
        }
    }

    abstract fun isDataEdited(): Boolean

    fun isNeedConfirmation(): Boolean {
        return !(saved || !isDataEdited() || discarded)
    }

    fun closeDialog(){
        hideKeyboard()
        if (isNeedConfirmation()) showDiscardDialog()
        else dismiss()
    }

    fun showDiscardDialog() {
        DiscardDialog(requireContext()).show { response ->
            if (response) {
                discarded = true
                dismiss()
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