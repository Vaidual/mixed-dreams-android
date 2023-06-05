package com.example.mixed_drems_mobile.api.auth

import com.example.mixed_drems_mobile.api.ApiRoutes
import com.example.mixed_drems_mobile.api.ApiClient.client
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import android.content.Context
import android.util.Log
import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.auth.login.LoginRequest
import com.example.mixed_drems_mobile.api.postWithApiResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

class AuthServiceImpl() : IAuthService {
    override suspend fun register(data: CustomerRegisterRequest): ApiResponse<AuthResponse> {

        return client.postWithApiResponse<AuthResponse>{
            url(ApiRoutes.RegisterCustomer)
            method = HttpMethod.Post
            setBody(data)
        }
    }

    override suspend fun login(data: LoginRequest): ApiResponse<AuthResponse> {

        return client.postWithApiResponse<AuthResponse>{
            url(ApiRoutes.Login)
            method = HttpMethod.Post
            setBody(data)
        }
    }
}