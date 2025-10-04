package com.example.freshkeeper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.freshkeeper.grocery.view.bottomnav.BottomNavBar

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            FreshKeeperNavGraph()
        }
    }
}
