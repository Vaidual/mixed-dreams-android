package com.example.mixed_drems_mobile.api.auth

import com.example.mixed_drems_mobile.api.ApiRoutes
import com.example.mixed_drems_mobile.api.ApiClient.client
import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.auth.login.LoginRequest
import com.example.mixed_drems_mobile.api.postWithApiResponse
import com.example.mixed_drems_mobile.utils.MainApplication
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRepositoryImpl: IAuthRepository {
    override suspend fun register(data: CustomerRegisterRequest): ApiResponse<AuthResponse> {

        val result = client.postWithApiResponse<AuthResponse> {
            url(ApiRoutes.RegisterCustomer)
            method = HttpMethod.Post
            setBody(data)
        }
        result.data?.tokens?.let { SharedPreferencesHelper.saveTokens(MainApplication.instance, it) }

        return result
    }

    override suspend fun login(data: LoginRequest): ApiResponse<AuthResponse> {

        val result = client.postWithApiResponse<AuthResponse> {
            url(ApiRoutes.Login)
            method = HttpMethod.Post
            setBody(data)
        }
        result.data?.tokens?.let { SharedPreferencesHelper.saveTokens(MainApplication.instance, it) }

        return result
    }
}