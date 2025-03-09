package com.example.recipes.components

//  Importation des composants Jetpack Compose (équivalent à React Native UI components)
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//  Déclaration d'un composant composable (équivalent à une fonction React qui retourne un JSX)
@Composable
fun RecipeFilters(selectedCategory: String, onCategorySelected: (String) -> Unit) {

    //  "Row" est l'équivalent d'un <div style={{ display: 'flex', flexDirection: 'row' }}>
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, // ✅ Espace les boutons de manière égale
        modifier = Modifier.fillMaxWidth() // ✅ La rangée prend toute la largeur de l'écran
    ) {
        //  Liste des catégories disponibles
        listOf("All", "Meat", "Cakes").forEach { category ->

            //  Chaque élément de la liste crée un bouton
            Button(
                onClick = { onCategorySelected(category) }, // ✅ Appelle la fonction onCategorySelected avec la catégorie sélectionnée
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface, // ✅ Change la couleur du bouton sélectionné
                    contentColor = if (selectedCategory == category) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface // ✅ Change la couleur du texte en fonction de la sélection
                ),
                border = if (selectedCategory == category) null
                else ButtonDefaults.outlinedButtonBorder // ✅ Ajoute une bordure aux boutons non sélectionnés
            ) {
                Text(category) // ✅ Affiche le texte de la catégorie sur le bouton
            }
        }
    }

    //  Ajoute un espacement vertical sous la ligne de boutons (comme margin-bottom en CSS)
    Spacer(modifier = Modifier.height(8.dp))
}
