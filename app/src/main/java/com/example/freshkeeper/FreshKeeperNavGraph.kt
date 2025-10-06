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
    groceryViewModel: GroceryViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inventory",
        //modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {

        // ---------- Inventory ----------
        composable(
            "inventory",
//            enterTransition = {
//                slideInHorizontally(
//                    initialOffsetX = { fullWidth -> fullWidth }, // from right
//                    animationSpec = tween(350, easing = FastOutSlowInEasing)
//                ) + fadeIn(animationSpec = tween(350))
//            },
//            exitTransition = {
//                slideOutHorizontally(
//                    targetOffsetX = { fullWidth -> -fullWidth }, // to left
//                    animationSpec = tween(350, easing = FastOutSlowInEasing)
//                ) + fadeOut(animationSpec = tween(350))
//            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }, // from left on back
                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(350))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }, // to right on back
                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) {
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

        // ---------- Recipe ----------
        composable(
            "recipe",
            enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(350)
            ) + fadeIn(tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(350)
            ) + fadeOut(tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(350)
            ) + fadeIn(tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(350)
            ) + fadeOut(tween(350)) }
        ) {
            RecipeScreen(navController = navController, recipeViewModel = recipeViewModel)
        }

        // ---------- Add/Edit Grocery ----------
        composable(
            "addGrocery/{id}",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }, // pop up from bottom
                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(450))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }, // slide down to bottom
                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(450))
            },
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight }, // from top on back
                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(450))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }, // slide down on back
                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(450))
            }
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val scope = rememberCoroutineScope()
            GroceryAddingScreen(
                viewModel = groceryViewModel,
                onSaveClicked = { groceryId ->
                    if (groceryViewModel.validateFields()) {
                        groceryViewModel.addGrocery(id ?: -1)
                        scope.launch {
                            delay(300) // allow animation to finish
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

        //recipe details
        composable(
            "recipeDetail/{mealId}",
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight }, // from top on back
                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(450))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }, // slide down on back
                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(450))
            }
        ) { backStackEntry ->

            val mealId = backStackEntry.arguments?.getString("mealId")
            val recipeViewModel: RecipeViewModel = hiltViewModel()
            val meal by recipeViewModel.selectedMeal.collectAsState()


            Log.i("mealHari", meal.toString())
            Log.i("mealHari", meal.toString())
            // Trigger loading when composable is first displayed
            LaunchedEffect(mealId) {
                recipeViewModel.getRecipeById(mealId)
            }

            RecipeDetailScreen(recipe = meal, navController = navController)
        }

    }
}
