package com.leviancode.android.gsmbox.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController

fun <T> MutableLiveData<List<T>>.addItem(item: T) {
    value?.toMutableList()?.let {
        it.add(item)
        value = it
    }
}

fun <T> MutableLiveData<List<T>>.removeItem(item: T) {
    value?.toMutableList()?.let {
        it.remove(item)
        value = it
    }
}

fun <T> MutableLiveData<List<T>>.update() {
    value?.toMutableList()?.let {
        value = it
    }
}

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}