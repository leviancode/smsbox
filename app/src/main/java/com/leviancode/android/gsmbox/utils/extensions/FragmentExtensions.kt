package com.leviancode.android.gsmbox.utils.extensions

import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.leviancode.android.gsmbox.utils.hideKeyboard


fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
fun <T> Fragment.removeNavigationResult(key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.remove<T>(key)
}

inline fun Fragment.navigate(action: () -> NavDirections){
    findNavController().navigate(action())
}

fun Fragment.goBack(){
   // hideKeyboard()
    findNavController().navigateUp()
}

fun Fragment.askPermission(permission: String, callback: (Boolean) -> Unit){
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

fun Fragment.showToast(message: String){
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 180)
    }.show()
}