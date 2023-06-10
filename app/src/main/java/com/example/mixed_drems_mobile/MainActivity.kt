package com.example.mixed_drems_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.pages.Login
import com.example.mixed_drems_mobile.pages.products.ProductsPage
import com.example.mixed_drems_mobile.pages.Signup
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MixeddremsmobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(activity = this)
                }
            }
        }
    }
}

@Composable
fun Navigator(activity: ComponentActivity) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (SharedPreferencesHelper.getAccessToken(activity) == null) Routes.Login.route else Routes.Products.route
    ) {
        composable(Routes.Login.route) {
            Login(navController = navController, activity)
        }
        composable(Routes.Signup.route) {
            Signup(navController = navController, activity)
        }
        composable(Routes.Products.route) {
            ProductsPage(navController = navController, activity)
        }
    }
}