package com.example.mixed_drems_mobile.api.auth

import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.auth.login.LoginRequest

interface IAuthRepository {
    suspend fun register(data: CustomerRegisterRequest): ApiResponse<AuthResponse>
    suspend fun login(data: LoginRequest): ApiResponse<AuthResponse>
}