package com.example.recipes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipes.models.Recipe
import com.example.recipes.components.RecipeCard
import com.example.recipes.components.RecipeSearchBar
import com.example.recipes.components.RecipeFilters
import com.example.recipes.repositories.RecipeRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(repository: RecipeRepository, onRecipeClick: (Recipe) -> Unit) {
    val recipes = remember { mutableStateListOf<Recipe>() }
    val listState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    var currentPage by rememberSaveable { mutableStateOf(1) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isPagingLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("All") }

    val localRecipes by repository.getLocalRecipes().collectAsState(initial = emptyList())

    fun loadRecipes(page: Int) {
        scope.launch {
            if (page == 1) isLoading = true else isPagingLoading = true

            val success = repository.fetchRecipes(page = page)
            if (success) {
                val newRecipes = repository.getLocalRecipes().firstOrNull() ?: emptyList()
                val filteredNewRecipes = newRecipes.filterNot { it in recipes }

                println(" Recettes récupérées depuis la bdd : $newRecipes")

                recipes.addAll(filteredNewRecipes)
            } else {
                errorMessage = "Erreur de chargement"
            }

            if (page == 1) isLoading = false else isPagingLoading = false
        }
    }

    // ✅ Chargement initial
    LaunchedEffect(localRecipes) {
        if (localRecipes.isNotEmpty() && recipes.isEmpty()) {
            recipes.addAll(localRecipes)
        } else {
            loadRecipes(currentPage)
        }
    }

    // ✅ Scroll bas : Charger la page suivante
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo) {
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        if (lastVisibleItemIndex >= recipes.size - 5 && !isPagingLoading) {
            currentPage++
            loadRecipes(currentPage)
        }
    }

    // ✅ Filtrage des recettes
    val filteredRecipes = recipes.filter { recipe ->
        val matchesQuery = searchQuery.isEmpty() || recipe.title.contains(searchQuery, ignoreCase = true)
        val matchesCategory = when (selectedCategory) {
            "Meat" -> listOf("pork", "chicken", "beef").any { it in recipe.title.lowercase() }
            "Cakes" -> listOf("cake", "cookie", "pie").any { it in recipe.title.lowercase() }
            else -> true
        }
        matchesQuery && matchesCategory
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Recettes de cuisine") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 🔍 Utilisation du composant `RecipeSearchBar`
            RecipeSearchBar(
                searchQuery = searchQuery,
                onSearchChange = {
                    searchQuery = it
                    selectedCategory = "All" // ✅ Réinitialise le filtre si on tape dans la barre de recherche
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 📌 Utilisation du composant `RecipeFilters`
            RecipeFilters(
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    searchQuery = "" // ✅ Réinitialise la recherche si on sélectionne un filtre
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading && recipes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe) })
                }

                if (isPagingLoading) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
