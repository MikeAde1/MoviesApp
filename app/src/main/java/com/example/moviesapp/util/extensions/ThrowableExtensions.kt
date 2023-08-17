package com.example.moviesapp.util.extensions

import com.example.moviesapp.data.api.NetworkUnavailableException
import org.json.JSONObject
import retrofit2.HttpException

fun Throwable.getErrorMessage(): String {
    return when (this) {
        is HttpException -> getHttpExceptionMessage()
        is NetworkUnavailableException -> NO_INTERNET_ERROR_MESSAGE
        else -> GENERIC_ERROR_MESSAGE
    }
}

private fun HttpException.getHttpExceptionMessage(): String {
    return try {
        val msg: String? = this.response()?.errorBody()?.string()
        msg?.let {
            JSONObject(msg).getString("status_message")
        } ?: GENERIC_ERROR_MESSAGE
    } catch (e: Exception) {
        GENERIC_ERROR_MESSAGE
    }
}

const val NO_INTERNET_ERROR_MESSAGE = "It seems your internet is not connected."
const val GENERIC_ERROR_MESSAGE = "An error occurred. Please try again"