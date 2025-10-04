package com.example.freshkeeper.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshkeeper.recipe.model.RecipeRepository
import com.example.freshkeeper.recipe.model.data.Meal
import com.example.freshkeeper.recipe.model.data.RecipeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<RecipeResponse?>(null)
    val recipes: StateFlow<RecipeResponse?> = _recipes

    private val _searchKey = MutableStateFlow("")
    val searchKey: StateFlow<String> = _searchKey

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        // Optional: load a default recipe on startup
        searchRecipe("chicken")
    }

    fun setSearchKey(key: String) {
        _searchKey.value = key
    }

    fun searchRecipe(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val response = recipeRepository.searchRecipes(query)
                if (response?.meals.isNullOrEmpty()) {
                    _errorMessage.value = "No recipes found for \"$query\""
                    _recipes.value = RecipeResponse(emptyList())
                } else {
                    _recipes.value = response
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load recipes. Please check your connection."
                _recipes.value = RecipeResponse(emptyList())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipeById(mealId: String): Meal? {
        return _recipes.value?.meals?.find { it.idMeal == mealId }
    }
}
