package com.example.freshkeeper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshkeeper.view.GroceryAddingScreen
import com.example.freshkeeper.view.InventoryScreen
import com.example.freshkeeper.viewmodel.GroceryViewModel

@Composable
fun GroceryNavGraph(viewModel: GroceryViewModel = viewModel()) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inventory"
    ) {

        composable("inventory") {
            InventoryScreen(
                viewModel = viewModel,
                onAddClick = { groceryId ->
                    if (groceryId == null) {
                        viewModel.clearAllStates() // Reset for new grocery
                        navController.navigate("addGrocery/-1")
                    } else {
                        viewModel.loadGroceryById(groceryId) // Preload for edit
                        navController.navigate("addGrocery/$groceryId")
                    }
                }
            )
        }


        composable("addGrocery/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            GroceryAddingScreen(
                viewModel = viewModel,
                onSaveClicked = { groceryId ->
                    if (id == -1)
                        viewModel.addGrocery(id)
                    else
                       viewModel.addGrocery(id)
                    if (groceryId != null){
                        viewModel.clearAllStates()
                        viewModel.deleteGroceryById(groceryId)
                    }

                    navController.popBackStack()

                },
                onBackClicked = {
                    navController.popBackStack()
                },
                groceryId = id
            )
        }

    }
}