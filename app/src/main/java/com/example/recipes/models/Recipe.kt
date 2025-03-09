package com.example.recipes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//  Indique que cette classe peut être convertie en JSON
@Serializable
//  Déclare cette classe comme une table en base de données Room
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val pk: Int, //  Clé primaire unique de la recette
    val title: String, //  Nom de la recette
    val publisher: String, //  Auteur de la recette
    @SerialName("featured_image") val imageUrl: String, //  Image de la recette (nom adapté depuis l'API)
    val rating: Int, //  Note de la recette (ex: 5 étoiles)
    @SerialName("source_url") val sourceUrl: String, //  Lien vers la recette originale
    val description: String?, //  Description (peut être null)
    @SerialName("cooking_instructions") val instructions: String?, //  Étapes de préparation (peut être null)
    val ingredients: List<String> //  Liste des ingrédients (nécessite un `TypeConverter` pour Room)
)

//  Modèle pour la réponse API contenant une liste de recettes
@Serializable
data class RecipeList(
    val results: List<Recipe>
)
