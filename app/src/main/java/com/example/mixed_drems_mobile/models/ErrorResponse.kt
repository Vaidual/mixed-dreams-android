package com.example.mixed_drems_mobile.models

import kotlinx.serialization.Serializable

@Serializable
class ErrorResponse(
    val title: String,
    val statusCode: Int,
    val errorCode: Int,
) {
}