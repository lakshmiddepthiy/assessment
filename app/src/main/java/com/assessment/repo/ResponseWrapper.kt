package com.assessment.repo

import com.assessment.common.Constants
import com.squareup.moshi.JsonClass
import retrofit2.Response


sealed class ResponseWrapper<out T : Any> {
    data class Success<T : Any>(
        val data: T? = null,
        val apiError: Response<T>? = null,
        val httpCode: Int? = null
    ) : ResponseWrapper<T>()

    data class Error<out T : Any>(val errorWrapper: ErrorWrapper, val httpCode: Int? = null) :
        ResponseWrapper<T>()
}

data class ErrorWrapper(
    val errorMessage: String = Constants.GENERIC_ERROR,
    val errorCode: String = "",
    val errorTitle: String = ""
)

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val errorCode: String = "",
    val errorHeader: String = "",
    val errorMessage: String = Constants.GENERIC_ERROR
)

class InternetNotAvailableException : Exception()