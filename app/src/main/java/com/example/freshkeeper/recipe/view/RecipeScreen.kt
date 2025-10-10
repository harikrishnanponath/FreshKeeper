package com.example.freshkeeper.recipe.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.freshkeeper.grocery.view.bottomnav.BottomNavBar
import com.example.freshkeeper.recipe.viewmodel.RecipeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel = hiltViewModel()
) {
    val searchKey by recipeViewModel.searchKey.collectAsState()
    val recipes by recipeViewModel.recipes.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Trigger API call when search key changes
    LaunchedEffect(searchKey) {
        if (searchKey.isNotEmpty()) {
            recipeViewModel.searchRecipe(searchKey)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Recipe",
                    style = typography.titleLarge.copy(color = colorScheme.onPrimary)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.primary,
                titleContentColor = colorScheme.onPrimary,
                navigationIconContentColor = colorScheme.onPrimary,
                actionIconContentColor = colorScheme.onPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔍 Search Bar
        OutlinedTextField(
            value = searchKey,
            onValueChange = { recipeViewModel.setSearchKey(it) },
            label = { Text("Search recipes", color = colorScheme.onSurfaceVariant) },
            trailingIcon = {
                IconButton(onClick = { recipeViewModel.searchRecipe(searchKey) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = colorScheme.onSurfaceVariant
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline,
                cursorColor = colorScheme.primary,
                focusedLabelColor = colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔄 Content Area
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = typography.bodyMedium
                    )
                }
            }

            recipes?.meals.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recipes found",
                        color = colorScheme.onSurfaceVariant,
                        style = typography.bodyMedium
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(recipes?.meals ?: emptyList()) { meal ->
                        RecipeCard(
                            meal = meal,
                            onClick = {
                                navController.navigate("recipeDetail/${meal.idMeal}")
                            }
                        )
                    }
                }
            }
        }
    }
}
