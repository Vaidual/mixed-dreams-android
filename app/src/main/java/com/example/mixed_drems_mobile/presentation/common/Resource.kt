package com.example.mixed_drems_mobile.presentation.common

import com.example.mixed_drems_mobile.models.ErrorResponse

sealed class Resource<T>(val data: T? = null, val error: ErrorResponse? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: ErrorResponse) : Resource<T>(error = error)
    class Loading<T>() : Resource<T>()
}