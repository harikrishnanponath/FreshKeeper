package com.example.freshkeeper

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshkeeper.view.GroceryAddingScreen
import com.example.freshkeeper.view.InventoryScreen
import com.example.freshkeeper.view.RecipeScreen
import com.example.freshkeeper.viewmodel.GroceryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GroceryNavGraph(
    viewModel: GroceryViewModel = viewModel()
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inventory"
    ) {

        // ---------- Inventory ----------
        composable(
            "inventory",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }, // from right
                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }, // to left
                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(350))
            },
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
                viewModel = viewModel,
                onAddClick = { groceryId ->
                    viewModel.clearAllStates()
                    if (groceryId == null) navController.navigate("addGrocery/-1")
                    else {
                        viewModel.loadGroceryById(groceryId)
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
            RecipeScreen(navController = navController)
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
                viewModel = viewModel,
                onSaveClicked = { groceryId ->
                    if (viewModel.validateFields()) {
                        viewModel.addGrocery(id ?: -1)
                        scope.launch {
                            delay(300) // allow animation to finish
                            navController.popBackStack()
                        }
                    }
                },
                onBackClicked = { navController.popBackStack() },
                onDeleteClicked = { groceryId ->
                    viewModel.clearAllStates()
                    viewModel.deleteGroceryById(groceryId)
                    navController.popBackStack()
                },
                groceryId = id,
                navController = navController
            )
        }

    }
}
