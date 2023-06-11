package com.example.mixed_drems_mobile.navigation

sealed class Routes(val route: String) {
    object Signup : Routes(route = "signup")
    object Login : Routes(route = "login")
    object Products : Routes(route = "products")
    object Orders : Routes(route = "orders")
    object Account : Routes(route = "account")
}