package com.example.freshkeeper.view.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Scale
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {

    object Inventory : BottomNavItem(
        route = "inventory",
        title = "Inventory",
        icon = Icons.Default.Home
    )

    object Recipe : BottomNavItem(
        route = "recipe",
        title = "Recipe ",
        icon = Icons.Default.Scale
    )

    object Profile: BottomNavItem(
        route = "Profile",
        title = "Profile",
        icon = Icons.Default.AccountCircle
    )


}