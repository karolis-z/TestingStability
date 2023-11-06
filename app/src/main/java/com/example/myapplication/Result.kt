package com.example.myapplication

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    @JvmInline
    value class Success<T>(val value: T) : Result<T>

    @JvmInline
    value class Error(val exception: Throwable? = null) : Result<Nothing>

    object Loading : Result<Nothing>
}

inline fun <R, T> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(value))
        is Result.Error -> this
        is Result.Loading -> this
    }
}

inline fun <R, T> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return when (this) {
        is Result.Success -> transform(value)
        is Result.Error -> this
        is Result.Loading -> this
    }
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}