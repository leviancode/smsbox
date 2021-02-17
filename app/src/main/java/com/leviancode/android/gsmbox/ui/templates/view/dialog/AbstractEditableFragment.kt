package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.extra.DiscardDialog
import com.leviancode.android.gsmbox.utils.KeyboardUtil

abstract class AbstractEditableFragment : Fragment() {
    var discarded = false
    var saved = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            scrimColor = Color.TRANSPARENT
            duration = 300
        }
    }

    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    abstract fun isDataEdited(): Boolean

    fun isNeedConfirmation(): Boolean {
        return !(saved || !isDataEdited() || discarded)
    }

    fun closeDialog(){
        hideKeyboard()
        if (isNeedConfirmation()) showDiscardDialog()
        else findNavController().navigateUp()
    }

    fun showDiscardDialog() {
        DiscardDialog(requireContext()).show { response ->
            if (response) {
                discarded = true
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

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}