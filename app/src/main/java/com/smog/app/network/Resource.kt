package com.smog.app.network

import com.smog.app.utils.AppError

data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val error: AppError? = null
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data = data)
        }

        fun <T> error(error: AppError): Resource<T> {
            return Resource(Status.ERROR, error = error)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}