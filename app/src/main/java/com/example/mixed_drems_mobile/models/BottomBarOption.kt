package com.example.mixed_drems_mobile.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mixed_drems_mobile.navigation.Routes

sealed class BottomBarOption(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarOption(
        route = Routes.Products.route,
        title = "Home",
        icon = Icons.Default.Home
    )

    object Orders: BottomBarOption(
        route = Routes.Orders.route,
        title = "Orders",
        icon = Icons.Default.ListAlt
    )

    object Account: BottomBarOption(
        route = Routes.Account.route,
        title = "Account",
        icon = Icons.Default.ManageAccounts
    )
}
