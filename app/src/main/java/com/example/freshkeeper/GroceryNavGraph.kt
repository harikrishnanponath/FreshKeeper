package com.example.freshkeeper

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshkeeper.view.GroceryAddingScreen
import com.example.freshkeeper.view.InventoryScreen
import com.example.freshkeeper.viewmodel.GroceryViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                        viewModel.clearAllStates()
                        viewModel.loadGroceryById(groceryId) // Preload for edit
                        navController.navigate("addGrocery/$groceryId")

                    }
                }
            )
        }


        composable("addGrocery/{id}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }, // from right
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }, // to left
                    animationSpec = tween(500)
                )
            }) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val scope = rememberCoroutineScope()
            GroceryAddingScreen(
                viewModel = viewModel,
                onSaveClicked = { groceryId ->
                    if (id == -1 && viewModel.validateFields()){
                        viewModel.addGrocery(id)
                        // ✅ add delay before navigating
                        scope.launch {
                            delay(500)
                            navController.popBackStack()
                        }

                    }
                    if (id != -1 && viewModel.validateFields()){
                        viewModel.addGrocery(id)
                        scope.launch {
                            delay(500)
                            navController.popBackStack()
                        }
                    }

                },
                onBackClicked = {
                    navController.popBackStack()
                },
                onDeleteClicked = { groceryId ->

                        navController.popBackStack()
                        viewModel.clearAllStates()
                        viewModel.deleteGroceryById(groceryId)


                },
                groceryId = id,
                navController = navController
            )
        }

    }
}