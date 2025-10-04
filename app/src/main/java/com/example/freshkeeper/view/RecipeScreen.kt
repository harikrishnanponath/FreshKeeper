package com.example.freshkeeper.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.freshkeeper.view.bottomnav.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("FreshKeeper") }) },
        bottomBar = { BottomNavBar(navController = navController) }

    ) { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding)) {

        }
    }
}