package com.example.recipes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RecipeSearchBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        label = { Text("Search a recipe...") },
        modifier = Modifier.fillMaxWidth()
    )
}
