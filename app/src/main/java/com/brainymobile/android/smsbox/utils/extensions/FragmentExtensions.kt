package com.brainymobile.android.smsbox.utils.extensions

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener


fun <T> Fragment.observeNavigationResult(key: String = "result", action: (T) -> Unit) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) { action(it) }

inline fun <reified T> Fragment.setNavigationResultListener(key: String = "result", crossinline action: (T) -> Unit) {
    setFragmentResultListener(key){ _, bundle ->
        val result = bundle[key]
        if (bundle[key] is T) {
            action(result as T)
        }
    }
}

fun Fragment.setNavigationResult(key: String = "result", result: Any) {
    setFragmentResult(key, bundleOf(key to result))
}

fun <T> Fragment.setNavigationResultLiveData(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.removeNavigationResult(key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.remove<T>(key)
}

inline fun Fragment.navigate(action: () -> NavDirections) {
    findNavController().navigate(action())
}

fun Fragment.navigateBack() {
    kotlin.runCatching {
        findNavController().navigateUp()
    }
}

fun Fragment.askPermission(permission: String, callback: (Boolean) -> Unit) {
    Dexter.withContext(requireContext())
        .withPermission(permission)
        .withListener(object : BasePermissionListener() {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                callback(true)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                callback(false)
            }
        }).check()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(messageRes: Int) {
    Toast.makeText(requireContext(), requireContext().getString(messageRes), Toast.LENGTH_SHORT)
        .show()
}

fun Fragment.showSnackbar(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        .show()
}

fun Fragment.showSnackbar(messageRes: Int) {
    Snackbar.make(requireView(), requireContext().getString(messageRes), Snackbar.LENGTH_LONG)
        .show()
}

fun Fragment.showSnackbarWithAction(message: String, actionText: String, callback: (View) -> Unit) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        .setAction(actionText) { callback(it) }
        .show()
}

fun Fragment.showSnackbarWithAction(messageRes: Int, actionTextRes: Int, callback: (View) -> Unit) {
    Snackbar.make(requireView(), requireContext().getString(messageRes), Snackbar.LENGTH_LONG)
        .setAction(requireContext().getString(actionTextRes)) { callback(it) }
        .show()
}
