package com.leviancode.android.gsmbox.utils

import android.util.Log
import android.widget.EditText
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun <T> MutableLiveData<List<T>>.addItem(item: T) {
    value?.toMutableList()?.let {
        it.add(item)
        value = it
    }
}

fun <T> MutableLiveData<List<T>>.updateItem(index: Int, item: T) {
    value?.toMutableList()?.let {
        if (index >= 0 && index < it.size) {
            it[index] = item
            value = it
        }
    }
}

fun <T> MutableLiveData<List<T>>.removeItem(item: T) {
    value?.toMutableList()?.let {
        it.remove(item)
        value = it
    }
}

fun <T> MutableLiveData<T>.update() {
    value = value
}

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
fun <T> Fragment.removeNavigationResult(key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.remove<T>(key)
}

inline fun <T, R> ObservableField<T>.map(crossinline transformFunc: (value: T?) -> R): ObservableField<R> =
    object : ObservableField<R>(this) {
        override fun get(): R? {
            return transformFunc(this@map.get())
        }
    }

inline fun <T> dependantObservableField(vararg dependencies: Observable, defaultValue: T? = null, crossinline mapper: () -> T?) =
    object : ObservableField<T>(*dependencies) {
        override fun get(): T? {
            return mapper()
        }
    }.apply { set(defaultValue) }

inline fun <T> dependantLiveData(vararg dependencies: LiveData<out Any>, defaultValue: T? = null, crossinline mapper: () -> T?): LiveData<T> =
    MediatorLiveData<T>().also { mediatorLiveData ->
        val observer = Observer<Any> { mediatorLiveData.value = mapper() }
        dependencies.forEach { dependencyLiveData ->
            mediatorLiveData.addSource(dependencyLiveData, observer)
        }
    }.apply { value = defaultValue }

fun isNotBlankLiveData(vararg dependencies: LiveData<out String>, defaultValue: Boolean = false): LiveData<Boolean> =
    MediatorLiveData<Boolean>().also { mediatorLiveData ->
        val observer = Observer<String> { mediatorLiveData.value = it.isNotBlank() }
        dependencies.forEach { dependencyLiveData ->
            mediatorLiveData.addSource(dependencyLiveData, observer)
        }
    }.apply { value = defaultValue }

fun isNotEmpty(vararg strings: String): Boolean = strings.count { it.isNotBlank() } == strings.size
fun isEmpty(vararg strings: String): Boolean = strings.count { it.isBlank() } == strings.size

inline fun <T>List<T>.ifNotEmpty(defaultValue: (List<T>) -> Unit){
    if (isNotEmpty()) defaultValue(this)
}

fun Fragment.navigate(action: () -> NavDirections){
    findNavController().navigate(action())
}

fun Fragment.goBack(){
    findNavController().navigateUp()
}

fun Fragment.log(message: String){
    Log.i("APP", message)
}
