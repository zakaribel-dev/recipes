package com.example.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.recipes.db.RecipeDatabase
import com.example.recipes.models.Recipe
import com.example.recipes.repositories.RecipeRepository
import com.example.recipes.screens.LoadingScreen
import com.example.recipes.ui.screens.RecipeDetailScreen
import com.example.recipes.ui.screens.RecipeListScreen
import com.example.recipes.ui.theme.RecipesTheme
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialisation de la base de données et du repository
        val database = RecipeDatabase.getDatabase(this)
        val recipeDao = database.recipeDao()
        val repository = RecipeRepository(recipeDao)

        setContent {
            RecipesTheme {
                var isLoading by rememberSaveable { mutableStateOf(true) }

                // ✅ Sauvegarde `selectedRecipe` sous forme de JSON string (obligatoire pour les objets complexes)
                var selectedRecipeJson by rememberSaveable { mutableStateOf<String?>(null) }
                val selectedRecipe: Recipe? = selectedRecipeJson?.let { Json.decodeFromString(it) }

                if (isLoading) {
                    LoadingScreen(onTimeout = { isLoading = false })
                } else {
                    if (selectedRecipe == null) {
                        RecipeListScreen(repository) { recipe ->
                            selectedRecipeJson = Json.encodeToString(recipe) // ✅ Sauvegarde la recette en JSON
                        }
                    } else {
                        RecipeDetailScreen(
                            recipe = selectedRecipe,
                            onBack = { selectedRecipeJson = null } // ✅ Retour à la liste
                        )
                    }
                }
            }
        }
    }
}
