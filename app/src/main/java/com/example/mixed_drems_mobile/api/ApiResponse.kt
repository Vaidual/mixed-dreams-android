package com.example.mixed_drems_mobile.api

import com.example.mixed_drems_mobile.models.ErrorResponse

data class ApiResponse<T>(
    val isSuccess: Boolean,
    val data: T?,
    val error: ErrorResponse?,
)

//sealed class ApiResponse<out T> {
//    data class Success<T>(val data: T) : ApiResponse<T>()
//    data class Failure(val error: ErrorResponse) : ApiResponse<Nothing>()
//}
//
//fun handleResponse(response: ApiResponse<String>) {
//    when (response) {
//        is ApiResponse.Success -> {
//            val data: String = response.data // No need for null-safe operator
//            // Handle success case
//        }
//        is ApiResponse.Failure -> {
//            val error = response.error
//            // Handle failure case
//        }
//    }
//}