package com.example.freshkeeper.recipe.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.freshkeeper.recipe.model.data.Meal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: Meal?,
    navController: NavController
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { recipe?.strMeal?.let { Text(it) } ?: Text("Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (recipe == null) {
                // Loading indicator in the center
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                ) {
                    // Recipe Image
                    AsyncImage(
                        model = recipe.strMealThumb,
                        contentDescription = recipe.strMeal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category & Area
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        recipe.strCategory?.let { Text(text = it, style = MaterialTheme.typography.labelMedium) }
                        recipe.strArea?.let { Text(text = it, style = MaterialTheme.typography.labelMedium) }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ingredients
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val ingredients = listOf(
                        recipe.strIngredient1 to recipe.strMeasure1,
                        recipe.strIngredient2 to recipe.strMeasure2,
                        recipe.strIngredient3 to recipe.strMeasure3,
                        recipe.strIngredient4 to recipe.strMeasure4,
                        recipe.strIngredient5 to recipe.strMeasure5,
                        recipe.strIngredient6 to recipe.strMeasure6,
                        recipe.strIngredient7 to recipe.strMeasure7,
                        recipe.strIngredient8 to recipe.strMeasure8,
                        recipe.strIngredient9 to recipe.strMeasure9,
                        recipe.strIngredient10 to recipe.strMeasure10
                    ).filter { !it.first.isNullOrEmpty() }

                    ingredients.forEach { (ingredient, measure) ->
                        Text(
                            text = "$ingredient - $measure",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instructions
                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    recipe.strInstructions?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
