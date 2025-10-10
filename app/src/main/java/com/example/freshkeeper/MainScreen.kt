package com.example.freshkeeper

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.freshkeeper.grocery.view.bottomnav.BottomNavBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("inventory", "recipe")

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // 👈 removes all system padding
        bottomBar = {
            if (showBottomBar) BottomNavBar(navController = navController)
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            FreshKeeperNavGraph(navController = navController)
        }
    }
}


