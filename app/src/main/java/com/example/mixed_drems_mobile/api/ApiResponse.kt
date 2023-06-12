package com.example.mixed_drems_mobile.api

import com.example.mixed_drems_mobile.models.ErrorResponse

data class ApiResponse<T>(
    val isSuccess: Boolean,
    val data: T?,
    val error: ErrorResponse?,
)