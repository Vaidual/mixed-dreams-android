package com.example.mixed_drems_mobile.api

object ApiRoutes {
    private const val BASE_URL = "https://10.0.2.2:7239/api"
    const val RegisterCustomer = "$BASE_URL/auth/register/customer"
    const val Login = "$BASE_URL/auth/login"
    const val Products = "$BASE_URL/products"
    const val CreateIntent = "$BASE_URL/orders/create-payment-intent"
    val ProductById = {id: String -> "$BASE_URL/products/$id"}
}