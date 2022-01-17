package com.brainymobile.android.smsbox.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.brainymobile.android.smsbox.ui.activities.MainActivity
import com.brainymobile.android.smsbox.ui.dialogs.alertdialogs.DiscardAlertDialog
import com.brainymobile.android.smsbox.utils.extensions.navigateBack
import com.brainymobile.android.smsbox.utils.hideKeyboard
import com.brainymobile.android.smsbox.utils.showKeyboard

abstract class BaseFragment<T : ViewDataBinding>(private val layId: Int) : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    protected open var bottomNavViewVisibility = View.GONE

    protected open val showConfirmationDialogBeforeQuit = false

    protected open val showKeyboardOnStarted = false

    override fun onResume() {
        super.onResume()
        setBottomNavVisibility()
        if (showKeyboardOnStarted){
            showKeyboard()
        }
    }

    private fun setBottomNavVisibility() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavVisibility(bottomNavViewVisibility)
        }
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            close()
        }
        onCreated()
    }

    fun close(){
        hideKeyboard()
        if (showConfirmationDialogBeforeQuit){
            DiscardAlertDialog(requireContext()).show { confirmed ->
                if (confirmed) {
                    navigateBack()
                }
            }
        } else {
            navigateBack()
        }
    }

    abstract fun onCreated()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}