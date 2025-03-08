package com.example.recipes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipes.models.Recipe
import kotlinx.coroutines.flow.Flow

// 📌 @Dao (Data Access Object) : Interface qui définit les opérations possibles sur la base de données Room
@Dao
interface RecipeDao {

    // 📌 Requête SQL pour récupérer toutes les recettes stockées dans la base de données
    // - `Flow<List<Recipe>>` → Retourne un **Flow** (permet de suivre les mises à jour en temps réel)
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>> // ✅ Retourne un **flux de données** mis à jour automatiquement

    // 📌 Insère une liste de recettes dans la base
    // - `OnConflictStrategy.REPLACE` → Si une recette existe déjà (même clé primaire), elle est **remplacée**
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>) // ✅ `suspend` car Room fonctionne en **asynchrone**

    // 📌 Supprime toutes les recettes de la base de données
    @Query("DELETE FROM recipes")
    suspend fun clearRecipes() // ✅ `suspend` car cette opération peut être longue et doit être exécutée en **background**
}
