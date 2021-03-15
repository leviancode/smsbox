package com.leviancode.android.gsmbox.utils

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

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

inline fun Fragment.navigate(action: () -> NavDirections){
    findNavController().navigate(action())
}

fun Fragment.goBack(){
    findNavController().navigateUp()
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

inline fun <reified T>List<T>.toJson() = Json.encodeToString(this)

fun String.toFile(dir: String, fileName: String): File {
    val file = File(dir, fileName)
    file.writeText(this)
    return file
}
fun String.toFile(dir: File, fileName: String): File {
    val file = File(dir, fileName)
    file.writeText(this)
    return file
}

fun File.copyTo(file: File) {
    inputStream().use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
}

fun File.copyTo(resolver: ContentResolver, uri: Uri) {
    inputStream().use { input ->
       resolver.openOutputStream(uri)?.use { output ->
           input.copyTo(output)
       }
    }
}

fun ContentResolver.copyFile(from: Uri, to: Uri) {
    openInputStream(from)?.use { input ->
        openOutputStream(to)?.use { output ->
            input.copyTo(output)
        }
    }
}

fun File.getMimeType(fallback: String = "image/*"): String {
    return MimeTypeMap.getFileExtensionFromUrl(toString())
        ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(toLowerCase(Locale.ROOT)) }
        ?: fallback // You might set it to */*
}

fun log(message: String){
    Log.i("APP", message)
}
