package com.example.mixed_drems_mobile.presentation.common

import com.example.mixed_drems_mobile.models.ErrorResponse

open class BaseState<T>(
    open val isLoading: Boolean = false,
    open val data: T? = null,
    open val error: ErrorResponse? = null
)