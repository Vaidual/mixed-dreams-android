package com.example.mixed_drems_mobile.api

object ApiRoutes {
    private const val BASE_URL = "https://10.0.2.2:7239/api"
    const val RegisterCustomer = "$BASE_URL/auth/register/customer"
    const val Login = "$BASE_URL/auth/login"
}