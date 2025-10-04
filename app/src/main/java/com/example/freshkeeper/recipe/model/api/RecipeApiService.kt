package com.example.freshkeeper.recipe.model.api

import com.example.freshkeeper.recipe.model.data.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {

    @GET("search.php")
    suspend fun searchRecipe(
        @Query("s") ingredient: String
    ): RecipeResponse?

    @GET("random.php")
    suspend fun randomRecipe(): RecipeResponse?

    @GET("filter.php")
    suspend fun filterByIngredient(
        @Query("i") ingredient: String
    ): RecipeResponse?

    @GET("lookup.php")
    suspend fun getRecipeById(
        @Query("i") id: String
    ): RecipeResponse?
}