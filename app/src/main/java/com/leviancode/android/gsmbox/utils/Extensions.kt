package com.leviancode.android.gsmbox.utils

import androidx.lifecycle.MutableLiveData

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