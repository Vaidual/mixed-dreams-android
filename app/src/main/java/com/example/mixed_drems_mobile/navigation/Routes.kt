package com.example.mixed_drems_mobile.navigation

sealed class Routes(val route: String) {
    object Signup : Routes(route = "signup")
    object Login : Routes(route = "login")
}