package com.assessment.repo

import com.google.gson.Gson
import com.assessment.common.Constants
import com.squareup.moshi.JsonDataException
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.reflect.KClass

open class BaseRepository {
    suspend fun <T : Any> submitRestApiCall(
        call: suspend () -> Response<T>,
        errorClass: KClass<*> = ErrorResponse::class
    ): ResponseWrapper<T> {
        var responseWrapper: ResponseWrapper<T>
        try {
            val response = call.invoke()
            responseWrapper = if (response.isSuccessful)
                ResponseWrapper.Success(data = response.body(), httpCode = response.code())
            else {
                ResponseWrapper.Error(
                    parseApiError(response.errorBody(), errorClass), response.code()
                )
            }
            return responseWrapper
        } catch (exception: Exception) {
            responseWrapper = errorHandling(exception)
        }
        return responseWrapper
    }


    private fun <T : Any> errorHandling(exception: Exception): ResponseWrapper<T> {
        return when (exception) {
            is InternetNotAvailableException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = Constants.INTERNET_ERROR
                )
            )
            is UnknownHostException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = Constants.ERROR_UNKNOWN_HOST
                )
            )
            is JsonDataException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = exception.message.toString()
                )
            )
            is SocketTimeoutException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = Constants.ERROR_TIMEOUT
                )
            )
            is HttpException -> {
                ResponseWrapper.Error(
                    ErrorWrapper(
                        errorMessage = exception.message.toString()
                    )
                )
            }
            is IOException -> {
                ResponseWrapper.Error(
                    ErrorWrapper(
                        errorMessage = exception.message.toString()
                    )
                )
            }
            else -> {
                ResponseWrapper.Error(
                    ErrorWrapper(
                        errorMessage = exception.message.toString()
                    )
                )
            }
        }
    }

    private fun parseApiError(error: ResponseBody?, clazz: KClass<*>): ErrorWrapper {
        val message =
            Gson().fromJson(error?.charStream(), ErrorResponse::class.java)

        return error?.string()?.let {
            if (it.isEmpty()) {
                return genericErrorMessage()
            }
            when (clazz) {
                ErrorResponse::class -> {
                    ErrorWrapper(
                        errorMessage = message.errorMessage.ifEmpty { Constants.GENERIC_ERROR },
                        errorCode = message.errorCode,
                        errorTitle = message.errorHeader
                    )
                }
                else -> genericErrorMessage()
            }
        } ?: genericErrorMessage()
    }

    private fun genericErrorMessage(): ErrorWrapper {
        return ErrorWrapper(
            errorMessage = Constants.GENERIC_ERROR
        )
    }
}