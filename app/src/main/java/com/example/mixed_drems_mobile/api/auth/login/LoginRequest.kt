package com.example.mixed_drems_mobile.api.auth.login

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean,
)
