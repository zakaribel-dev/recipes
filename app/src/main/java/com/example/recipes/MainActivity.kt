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

        //  Initialisation de la base de données et du repository
        val database = RecipeDatabase.getDatabase(this)
        val recipeDao = database.recipeDao()
        val repository = RecipeRepository(recipeDao)

        setContent {
            RecipesTheme {
                var isLoading by rememberSaveable { mutableStateOf(true) }

                //  Stockage de la recette sélectionnée sous forme de JSON
                // - Jetpack Compose **perd l’état après une rotation d’écran** si on stocke un objet complexe (`Recipe`).
                // - `rememberSaveable` ne peut **sauvegarder que des types simples** (String, Int, Boolean…).
                // - Solution 👉 On **convertit l’objet `Recipe` en JSON** (qui est une `String`).
                var selectedRecipeJson by rememberSaveable { mutableStateOf<String?>(null) }

                //  Transformation du JSON en objet `Recipe` après une rotation
                // - Si `selectedRecipeJson` est `null`, alors `selectedRecipe` sera aussi `null`.
                // - Sinon, on **reconvertit la chaîne JSON en un objet `Recipe`**.
                val selectedRecipe: Recipe? = selectedRecipeJson?.let { Json.decodeFromString(it) }

                if (isLoading) {
                    // 🔹 Affichage de l’écran de chargement pendant 2 secondes
                    LoadingScreen({ isLoading = false }) // grâce au callback, isLoading va me revenir à false au bout de 2 secondes.
                } else {
                    if (selectedRecipe == null) {
                        // 🔹 Affichage de la liste des recettes
                        RecipeListScreen(repository) { recipe ->
                            //  Stockage de la recette sous forme de JSON avant d'afficher l’écran de détails
                            // - Si on stockait `recipe` directement (sans JSON), il serait perdu après une rotation d’écran.
                            selectedRecipeJson = Json.encodeToString(recipe)
                        }
                    } else {
                        // 🔹 Affichage des détails de la recette sélectionnée
                        RecipeDetailScreen(
                            recipe = selectedRecipe,
                            onBack = { selectedRecipeJson = null } //  Retour à la liste
                        )
                    }
                }
            }
        }
    }
}
