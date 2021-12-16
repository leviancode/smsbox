package com.leviancode.android.gsmbox.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard

abstract class BaseBottomSheet<T : ViewDataBinding>(private val layId: Int) : BottomSheetDialogFragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    protected open val showKeyboardOnStarted = false

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (showKeyboardOnStarted){
            showKeyboard()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreated()
    }

    fun close(){
        hideKeyboard()
        dismiss()
    }

    abstract fun onCreated()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}