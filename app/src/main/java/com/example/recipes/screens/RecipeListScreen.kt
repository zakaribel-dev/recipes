package com.example.recipes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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

@OptIn(ExperimentalMaterial3Api::class) // 📌 Indique que la fonction utilise une API expérimentale de Material3
@Composable
fun RecipeListScreen(repository: RecipeRepository, onRecipeClick: (Recipe) -> Unit) {
    // 📌 Stocke la liste des recettes affichées à l’écran (État mutable)
    val recipes = remember { mutableStateListOf<Recipe>() }

    // 📌 Gère l'état du scroll de la grille
    val listState = rememberLazyGridState()

    // 📌 Permet de lancer des coroutines dans Composable
    val scope = rememberCoroutineScope()

    // ✅ Variables d’état pour la gestion de la pagination et du chargement
    var currentPage by rememberSaveable { mutableStateOf(1) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isPagingLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // ✅ Variables d’état pour la barre de recherche et les filtres
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("All") }

    // ✅ Récupération des recettes locales stockées en base
    val localRecipes by repository.getLocalRecipes().collectAsState(initial = emptyList())

    fun loadRecipes(page: Int) {
        scope.launch { // 🚀 Lancement de la coroutine pour exécuter le chargement en arrière-plan

            // ✅ Indique à l’UI qu’un chargement est en cours
            if (page == 1) {
                isLoading = true
            } else {
                isPagingLoading = true
            }

            // 🔹 Charge les recettes depuis l’API
            val success = repository.fetchRecipes(page.toString())

            if (success) {
                // 🔹 Récupère les recettes stockées en base après mise à jour
                val newRecipes = repository.getLocalRecipes().firstOrNull()

                if (newRecipes != null) {
                    val filteredNewRecipes = mutableListOf<Recipe>()

                    // 🔹 Filtrage : On ajoute uniquement les nouvelles recettes qui ne sont pas encore affichées
                    for (recipe in newRecipes) {
                        if (!recipes.contains(recipe)) {
                            filteredNewRecipes.add(recipe)
                        }
                    }

                    // 🔹 Ajoute les nouvelles recettes à la liste affichée dans l'UI
                    recipes.addAll(filteredNewRecipes)
                }
            } else {
                // ❌ Affiche un message si l’API échoue
                errorMessage = "Erreur de chargement"
            }

            // ✅ Désactive l’indicateur de chargement
            if (page == 1) {
                isLoading = false
            } else {
                isPagingLoading = false
            }
        }
    }


    // ✅ Chargement initial (si la base de données contient déjà des recettes, on les affiche)
    LaunchedEffect(localRecipes) {
        if (localRecipes.isNotEmpty() && recipes.isEmpty()) {
            recipes.addAll(localRecipes)
        } else {
            loadRecipes(currentPage)
        }
    }



    val filteredRecipes = recipes.filter { recipe ->
        val matchesQuery: Boolean

        if (searchQuery.isEmpty()) {
            matchesQuery = true // Si la barre de recherche est vide, on affiche toutes les recettes par défaut
        } else {
            // Sinon, on vérifie si le titre contient le texte recherché
            matchesQuery = recipe.title.contains(searchQuery, ignoreCase = true)
            // par exemple si je tape  burger dans la barre de recherche matchesQuery sera false
            // puisqu'il n'ya pas de titre contenant burger dans ce qui est envoyé par l'api
        }

        // A SAVOIR QUE SI JE TAPE QUELQUE CHOSE DANS LABARRE DE RECHERCHE , MATCHESCATEGORY PASSE A TRUE (voir ligne 162)

        val matchesCategory = when (selectedCategory) {
            "Meat" -> listOf("pork", "chicken", "beef").any { it in recipe.title.lowercase() }
            "Cakes" -> listOf("cake", "cookie", "pie").any { it in recipe.title.lowercase() }
            else -> true // si true alors pas de categorie selectionnée donc on affiche tout
        }
        matchesQuery && matchesCategory
        // pti exemple si je tape rien dans la barre de recherche et que je veux juste cliquer sur un bouton de filtre "Meat" Par exemple
        // bah matchesQuery est true (voir ligne 128) et matchesCategory aussi car j'ai cliqué sur le filtre "Meat" donc les deux expressions
        // matchesQuery && matchesCategory sont true, la methode .filter va garder les recettes correspondantes à ce qui a match

// Le seul cas de figure où l'une des expression est false
        // c'est si j'ai tapé quelque chose dans la barre de recherche qui correspond à aucun title de recette
    }

//  Détection du scroll en bas de la liste pour charger plus de recettes
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo) {
        //  Récupère la liste des éléments actuellement visibles dans la grille
        val visibleItems = listState.layoutInfo.visibleItemsInfo

        if (visibleItems.isNotEmpty()) { // Vérifie s'il y a au moins un élément visible (évite les erreurs)

            val lastItem = visibleItems.last() // Prend le dernier élément visible dans la grille
            val lastVisibleItemIndex = lastItem.index // Récupère son index (sa position dans la liste)

            // 📌 Vérifie si l'utilisateur a scrollé suffisamment pour déclencher le chargement de la prochaine page
            if (lastVisibleItemIndex >= filteredRecipes.size - 5 && !isPagingLoading) {
                //filteredRecipes et pas juste recipes pour prendre en compte "All"

                //  Exemple concret avec 30 recettes par page :
                // - `recipes.size = 30` → `recipes.size - 5 = 25`
                // - Si l'utilisateur scrolle jusqu'à `index >= 25`, la **page suivante est chargée**
                // - Ensuite, `recipes.size = 60`, donc la nouvelle limite devient `55`, etc.

                currentPage++ //  Incrémente le numéro de page pour charger les prochaines recettes
                loadRecipes(currentPage) // Charge les recettes de la nouvelle page via l’API
            }
        }
    }




    // Structure principale de l’écran (Barre d’app, recherche, filtres et grille des recettes)
    Scaffold(
        topBar = { TopAppBar(title = { Text("Kitchen recipes") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 🔍 Barre de recherche
            RecipeSearchBar(
                searchQuery = searchQuery,
                onSearchChange = {
                    searchQuery = it
                    selectedCategory = "All" // ✅ Réinitialise le filtre si l’utilisateur tape une recherche
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            //  Filtres pour choisir la catégorie des recettes
            RecipeFilters(
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    searchQuery = "" // ✅ Réinitialise la recherche quand un filtre est sélectionné
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            //  Affichage d’un loader si on charge les recettes pour la première fois
            if (isLoading && recipes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (filteredRecipes.isEmpty()) {
                Text(
                    text = "Aucune recette trouvée.",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // ✅ Affichage d’un message d’erreur si l’API échoue
            errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            // 📌 Affichage des recettes sous forme de grille
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe) })
                }

                // ✅ Loader en bas de la liste si une nouvelle page est en cours de chargement
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
