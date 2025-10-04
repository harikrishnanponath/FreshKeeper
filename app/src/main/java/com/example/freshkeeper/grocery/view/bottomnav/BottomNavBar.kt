package com.example.freshkeeper.grocery.view.bottomnav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Inventory,
        BottomNavItem.Recipe,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = Color.Black,
        tonalElevation = 0.dp
    ) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF50C878), // Emerald Green
                    selectedTextColor = Color(0xFF50C878),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )

        }
    }

}