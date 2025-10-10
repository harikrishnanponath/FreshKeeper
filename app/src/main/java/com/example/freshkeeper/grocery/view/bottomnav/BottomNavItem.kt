package com.example.freshkeeper.grocery.view.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.ui.graphics.vector.ImageVector
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val filledIcon: ImageVector // New property for selected/filled icon
) {

    object Inventory : BottomNavItem(
        route = "inventory",
        title = "Inventory",
        icon = Icons.Outlined.Home,
        filledIcon = Icons.Filled.Home
    )

    object Recipe : BottomNavItem(
        route = "recipe",
        title = "Recipe",
        icon = Icons.Outlined.Scale,
        filledIcon = Icons.Filled.Scale // Use filled version if available
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Outlined.AccountCircle,
        filledIcon = Icons.Filled.AccountCircle
    )
}
