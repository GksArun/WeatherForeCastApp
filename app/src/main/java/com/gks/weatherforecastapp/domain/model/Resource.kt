package com.gks.weatherforecastapp.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

sealed class Resource<out T> {
    data class Success<T>(val value: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

/** Extension to convert Retrofit calls to Resource flows */
inline fun <T> toResource(
    crossinline block: suspend () -> T,
): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    val value = block()
    emit(Resource.Success(value))
}.catchError()

/** Extension to catch common Retrofit exceptions and return error instead */
fun <T> Flow<Resource<T>>.catchError(): Flow<Resource<T>> {
    return catch { cause ->
        emit(
            Resource.Error(cause.message ?: "API Error")
        )
    }
}