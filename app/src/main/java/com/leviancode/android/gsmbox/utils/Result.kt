package com.leviancode.android.gsmbox.utils

sealed class Result<T>(data: T? = null) {
    object Loading : Result<Nothing>()
    class Success<T>(data: T? = null): Result<T>(data)
    class Failure(message: String = ""): Result<Nothing>()
}