package com.example.mixed_drems_mobile.pages

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mixed_drems_mobile.models.BottomBarOption
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.pages.account.AccountPage
import com.example.mixed_drems_mobile.pages.auth.login.Login
import com.example.mixed_drems_mobile.pages.auth.signup.Signup
import com.example.mixed_drems_mobile.pages.orders.OrdersPage
import com.example.mixed_drems_mobile.pages.product.ProductPage
import com.example.mixed_drems_mobile.pages.products.ProductsPage
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainPage(
    activity: ComponentActivity
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) },
        //modifier =  Modifier.safeDrawingPadding(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            Navigator(activity = activity, navController = navController)
        }
    }
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//            ) {
//                Navigator(activity = activity, navController = navController)
//                BottomBar(navController)
//            }
//        }
}

@Composable
fun Navigator(activity: ComponentActivity, navController: NavHostController) {
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
            ProductsPage(navController = navController)
        }
        composable(route = Routes.Orders.route) {
            OrdersPage(navController = navController)
        }
        composable(route = Routes.Account.route) {
            AccountPage(navController = navController)
        }
        composable(route = Routes.Product.route) { backStackEntry ->
            ProductPage(
                navigateBack = { navController.navigateUp() },
                backStackEntry.arguments?.getString("productId")!!
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarOption.Home,
        BottomBarOption.Orders,
        BottomBarOption.Account,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.secondary
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarOption,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    BottomNavigationItem(
        label = { Text(text = screen.title) },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation icon",
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        unselectedContentColor = LocalContentColor.current.copy(alpha = 0.5f)
    )
}
