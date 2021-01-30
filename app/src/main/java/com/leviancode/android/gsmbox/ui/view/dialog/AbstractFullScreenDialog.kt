package com.leviancode.android.gsmbox.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.leviancode.android.gsmbox.R

abstract class AbstractFullScreenDialog : DialogFragment() {
    var discarded = false
    abstract var saved: Boolean

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
        showKeyboard()
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            window?.setLayout(width, height)
            window?.setWindowAnimations(R.style.Theme_GsmBox_Slide)
        }
    }

    abstract fun isFieldsEmpty(): Boolean

    fun isNeedConfirmation(): Boolean {
        return !(saved || isFieldsEmpty() || discarded)
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

    fun showKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}