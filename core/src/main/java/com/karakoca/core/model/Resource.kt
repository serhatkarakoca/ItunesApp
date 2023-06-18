package com.karakoca.core.model

sealed class Resource<out T> {
    data class Success<T>(val data: T?): Resource<T>()
    data class Error(val error: Throwable): Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
