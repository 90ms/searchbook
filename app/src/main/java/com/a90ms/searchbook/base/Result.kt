package com.a90ms.searchbook.base

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val httpCode: Int,
        val errorCode: String,
        val message: String
    ) : Result<Nothing>()
    data class Exception(val exception: kotlin.Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Exception -> "Exception[exception=$exception]"
            is Error -> "Error[httpCode=$httpCode, errorCode=$errorCode, message=$message]"
        }
    }
}

inline fun <reified T> Result<T>.onSuccess(action: (data: T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }

    return this
}

/**
 * api 호출에서 httpcode 2xx 외에는 공통처리 되므로 2xx일때만 실행
 */
inline fun <reified T> Result<T>.onError(
    action: (code: String, message: String) -> Unit
): Result<T> {
    if (this is Result.Error && httpCode in 200..299) {
        action(errorCode, message)
    }

    return this
}

inline fun <reified T> Result<T>.onException(
    action: (exception: Exception) -> Unit
): Result<T> {
    if (this is Result.Exception) {
        action(exception)
    }

    return this
}
