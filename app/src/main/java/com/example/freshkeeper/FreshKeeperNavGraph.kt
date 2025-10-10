package com.example.freshkeeper

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshkeeper.grocery.view.GroceryAddingScreen
import com.example.freshkeeper.grocery.view.InventoryScreen
import com.example.freshkeeper.recipe.view.RecipeScreen
import com.example.freshkeeper.grocery.viewmodel.GroceryViewModel
import com.example.freshkeeper.recipe.view.RecipeDetailScreen
import com.example.freshkeeper.recipe.viewmodel.RecipeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FreshKeeperNavGraph(
    navController: NavHostController,
    groceryViewModel: GroceryViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = "inventory",
    ) {
        // Inventory
        composable("inventory") {
            InventoryScreen(
                viewModel = groceryViewModel,
                onAddClick = { groceryId ->
                    groceryViewModel.clearAllStates()
                    if (groceryId == null) navController.navigate("addGrocery/-1")
                    else {
                        groceryViewModel.loadGroceryById(groceryId)
                        navController.navigate("addGrocery/$groceryId")
                    }
                },
                navController = navController
            )
        }

        // Recipe
        composable("recipe") {
            RecipeScreen(navController = navController, recipeViewModel = recipeViewModel)
        }

        // Add/Edit Grocery
        composable("addGrocery/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val scope = rememberCoroutineScope()

            GroceryAddingScreen(
                viewModel = groceryViewModel,
                onSaveClicked = {
                    if (groceryViewModel.validateFields()) {
                        groceryViewModel.addGrocery(id ?: -1)
                        scope.launch {
                            delay(300)
                            navController.popBackStack()
                        }
                    }
                },
                onBackClicked = { navController.popBackStack() },
                onDeleteClicked = { groceryId ->
                    groceryViewModel.clearAllStates()
                    groceryViewModel.deleteGroceryById(groceryId)
                    navController.popBackStack()
                },
                groceryId = id,
                navController = navController
            )
        }

        // Recipe Details
        composable("recipeDetail/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            val recipeViewModel: RecipeViewModel = hiltViewModel()
            val meal by recipeViewModel.selectedMeal.collectAsState()

            LaunchedEffect(mealId) {
                recipeViewModel.getRecipeById(mealId)
            }

            RecipeDetailScreen(recipe = meal, navController = navController)
        }
    }
}
