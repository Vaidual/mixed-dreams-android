package com.example.mixed_drems_mobile.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class CustomerRegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)