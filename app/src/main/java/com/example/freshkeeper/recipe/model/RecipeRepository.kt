package com.example.freshkeeper.recipe.model

import com.example.freshkeeper.recipe.model.api.RecipeApiService
import com.example.freshkeeper.recipe.model.data.Meal
import com.example.freshkeeper.recipe.model.data.RecipeResponse
import jakarta.inject.Inject


class RecipeRepository @Inject constructor(
    private val recipeApiService: RecipeApiService

){
    suspend fun searchRecipes(query: String) = recipeApiService.searchRecipe(query)

    suspend fun getRandomRecipe() = recipeApiService.randomRecipe()

    suspend fun filterByIngredient(ingredient: String) = recipeApiService.filterByIngredient(ingredient)

    suspend fun getRecipeById(id: String?): Meal? {
        if (id.isNullOrEmpty()) return null
        val response = recipeApiService.getRecipeById(id)
        return response?.meals?.firstOrNull()
    }

}