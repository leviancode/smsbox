package com.leviancode.android.gsmbox.utils.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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
    hideKeyboard()
    findNavController().navigateUp()
}