package com.leviancode.android.gsmbox.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T> Flow<T>.collect(lifecycleOwner: LifecycleOwner, action: (T) -> Unit){
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        collect { action(it) }
    }
}