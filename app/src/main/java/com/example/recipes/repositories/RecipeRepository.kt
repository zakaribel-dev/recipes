package com.example.recipes.repositories

import android.util.Log
import com.example.recipes.db.RecipeDao
import com.example.recipes.models.Recipe
import com.example.recipes.models.RecipeList
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class RecipeRepository(private val recipeDao: RecipeDao) {
    private val client = HttpClient(OkHttp)
    private val apiUrl = "https://food2fork.ca/api/recipe/search/"
    private val apiKey = "9c8b06d329136da358c2d00e76946b0111ce2c48"

    // ✅ Récupérer les recettes stockées en local (Room Database)
    fun getLocalRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    // ✅ Charger les recettes depuis l'API et les stocker en base
    suspend fun fetchRecipes(query: String = "", page: Int = 1): Boolean {
        val url = "$apiUrl?page=$page&query=$query"
        return try {
            val response: String = client.get(url) {
                headers["Authorization"] = "Token $apiKey"
            }.bodyAsText()

            Log.i("RecipeRepository", "Réponse brute API : $response")

            val recipeList = Json { ignoreUnknownKeys = true }.decodeFromString<RecipeList>(response)

            val recipes = recipeList.results

            Log.i("RecipeRepository", "Données après parsing : $recipes")

            recipeDao.clearRecipes()
            recipeDao.insertRecipes(recipes)

            true
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Erreur lors du chargement des recettes : ${e.localizedMessage}")
            false
        }
    }
}
