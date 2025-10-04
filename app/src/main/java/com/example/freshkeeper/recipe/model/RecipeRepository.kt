package com.example.freshkeeper.recipe.model

import com.example.freshkeeper.recipe.model.api.RecipeApiService
import jakarta.inject.Inject


class RecipeRepository @Inject constructor(
    private val recipeApiService: RecipeApiService

){
    suspend fun searchRecipes(query: String) = recipeApiService.searchRecipe(query)

    suspend fun getRandomRecipe() = recipeApiService.randomRecipe()

    suspend fun filterByIngredient(ingredient: String) = recipeApiService.filterByIngredient(ingredient)

    suspend fun getRecipeById(id: String) = recipeApiService.getRecipeById(id)


}