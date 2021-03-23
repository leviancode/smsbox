package com.leviancode.android.gsmbox.utils.extensions

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

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
