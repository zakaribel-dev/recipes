package com.example.recipes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecipeFilters(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        listOf("All", "Meat", "Cakes").forEach { category ->
            Button(
                onClick = { onCategorySelected(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface,
                    contentColor = if (selectedCategory == category) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                ),
                border = if (selectedCategory == category) null
                else ButtonDefaults.outlinedButtonBorder
            ) {
                Text(category)
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
