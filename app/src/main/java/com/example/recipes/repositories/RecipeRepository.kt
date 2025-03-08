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

// 📌 RecipeRepository : Gestion des données de l'application
// - Permet d'accéder aux recettes **stockées en local** (Room) et **celles récupérées depuis l'API**
class RecipeRepository(private val recipeDao: RecipeDao) {

    // ✅ Initialisation du client HTTP Ktor (utilise OkHttp comme moteur)
    private val client = HttpClient(OkHttp)

    // 📌 URL de l'API et clé d'authentification
    private val apiUrl = "https://food2fork.ca/api/recipe/search/" // URL pour récupérer les recettes
    private val apiKey = "9c8b06d329136da358c2d00e76946b0111ce2c48" // Clé API (⚠️ Normalement, elle doit être sécurisée)

    // ✅ Récupérer les recettes stockées localement (base de données Room)
    fun getLocalRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()
    // - Retourne un **Flow** : permet d'obtenir des mises à jour automatiques en temps réel

    // ✅ Charger les recettes depuis l'API et les enregistrer dans la base de données locale
    suspend fun fetchRecipes(query: String = "", page: Int = 1): Boolean {
        val url = "$apiUrl?page=$page&query=$query" // 📌 Construire l'URL avec les paramètres

        return try {
            // 📌 Requête HTTP GET vers l'API avec l'authentification
            val response: String = client.get(url) {
                headers["Authorization"] = "Token $apiKey" // Envoi de la clé API
            }.bodyAsText() // Récupère la réponse sous forme de texte (JSON brut)

            // ✅ Log de la réponse brute (utile pour le debug)
            Log.i("RecipeRepository", "Réponse brute API : $response")

            // 📌 Conversion du JSON en objet `RecipeList`
            val recipeList = Json { ignoreUnknownKeys = true }.decodeFromString<RecipeList>(response)

            val recipes = recipeList.results // 📌 Liste des recettes extraites du JSON

            // ✅ Log après parsing (vérification des données)
            Log.i("RecipeRepository", "Données après parsing : $recipes")

            // 📌 Mise à jour de la base de données locale :
            recipeDao.clearRecipes()   // ❌ Supprime les anciennes recettes pour éviter les doublons
            recipeDao.insertRecipes(recipes) // ✅ Insère les nouvelles recettes récupérées

            true // ✅ Retourne `true` si tout s'est bien passé
        } catch (e: Exception) {
            // ❌ Gestion des erreurs en cas d'échec de la requête
            Log.e("RecipeRepository", "Erreur lors du chargement des recettes : ${e.localizedMessage}")
            false // ❌ Retourne `false` en cas d'échec
        }
    }
}
