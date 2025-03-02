package com.example.recipes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val pk: Int,
    val title: String,
    val publisher: String,
    @SerialName("featured_image") val imageUrl: String,
    val rating: Int,
    @SerialName("source_url") val sourceUrl: String,
    val description: String?,
    @SerialName("cooking_instructions") val instructions: String?,
    val ingredients: List<String> // 🔥 Room gérera la conversion avec le `TypeConverter`
)

@Serializable
data class RecipeList(
    val results: List<Recipe>
)
