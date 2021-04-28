package com.leviancode.android.gsmbox.ui.extra

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DiscardAlertDialog
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult

abstract class AbstractFullScreenEditableDialog<T : ViewDataBinding>(private val layId: Int) : DialogFragment() {
    private lateinit var binding: T

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), R.style.AppTheme_FullScreenDialog){
            override fun onBackPressed() {
                closeDialog(RESULT_CANCEL)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = DataBindingUtil.inflate(inflater, layId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(binding)
    }

    abstract fun setupViews(binding: T)

    abstract fun isDataEdited(): Boolean

    fun closeDialog(result: String){
        hideKeyboard()
        when (result) {
            RESULT_OK -> {
                setNavigationResult(true, REQ_SAVE_RECIPIENT)
                goBack()
            }
            RESULT_CANCEL -> {
                if (isDataEdited()) {
                    showDiscardDialog()
                } else {
                    setNavigationResult(false, REQ_SAVE_RECIPIENT)
                    goBack()
                }
            }
        }
    }

    fun showDiscardDialog() {
        DiscardAlertDialog.show(requireContext()) { response ->
            if (response) {
                setNavigationResult(false, REQ_SAVE_RECIPIENT)
                goBack()
            }
        }
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}