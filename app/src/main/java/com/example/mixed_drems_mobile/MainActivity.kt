package com.example.mixed_drems_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mixed_drems_mobile.models.BottomBarOption
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.pages.MainPage
import com.example.mixed_drems_mobile.pages.auth.login.Login
import com.example.mixed_drems_mobile.pages.products.ProductsPage
import com.example.mixed_drems_mobile.pages.auth.signup.Signup
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainPage(this)
        }
    }
}

