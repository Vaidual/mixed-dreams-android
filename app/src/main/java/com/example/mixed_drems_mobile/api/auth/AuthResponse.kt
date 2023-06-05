package com.example.mixed_drems_mobile.api.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: UserDto,
    val tokens: TokensDto,
)

@Serializable
class UserDto (
    val firstName: String,
    val lastName: String,
    val email: String,
    val roles: List<String>,
)

@Serializable
class TokensDto(
    val accessToken: String,
)