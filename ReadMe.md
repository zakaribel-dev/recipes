#  Recipe Explorer - Application de Recettes de Cuisine

##  Présentation du Projet
Recipe Explorer est une application mobile développée en **Kotlin avec Jetpack Compose**, permettant aux utilisateurs de **parcourir, rechercher et filtrer des recettes de cuisine**.

Elle utilise une **base de données locale (Room) avec synchronisation API**, permettant un **fonctionnement hors-ligne** si des recettes ont déjà été consultées.

---

##  Fonctionnalités Principales

###  Affichage et navigation
- **Écran de chargement** avec un logo personnalisé.
- **Liste des recettes disponibles**, affichant **titre, image et auteur**.
- **Accès aux détails d’une recette** en appuyant dessus.
- **Affichage des images des recettes** directement dans la liste.

###  Recherche et filtrage avancés
- **Barre de recherche** permettant de filtrer les recettes par **titre**.
- **Filtres par catégories** (ex: **Meat, Cakes, All**) via des boutons interactifs.

###  Chargement dynamique et pagination
- **Chargement automatique des pages suivantes** lorsque l’utilisateur atteint le bas de la liste.
- **Défilement fluide** grâce à une gestion optimisée du scroll.

###  Gestion des données et mode hors-ligne
- **Stockage des recettes en local** via **Room Database**.
- **Vérification régulière des mises à jour** et synchronisation avec l’API.
- **Mode hors-ligne** : les recettes restent accessibles si l’application a déjà eu accès au réseau.

###  Architecture organisée en couches
L’application est organisée en **plusieurs modules**, facilitant **la maintenabilité** et **l’évolutivité** :

- `components/`
- `db/`
- `models/`
- `repositories/`
- `screens/`
- `MainActivity.kt`

---

##  Barème d'évaluation (40 points)
Ci-dessous, une checklist basée sur le **barème d'évaluation**, avec pour les fonctionnalités réalisées, et **références au code**.

| Fonctionnalité | Points | Statut | Justification & Références au Code |
|---------------|--------|------|------------------------------------|
| **Écran de chargement avec un logo personnalisé** | 2 pts |  Fait | **Fichier :** `MainActivity.kt` (Splash Screen avant chargement des recettes). |
| **Affichage de la liste des recettes disponibles** | 4 pts | Fait | **Fichier :** `screens/RecipeListScreen.kt` (Utilisation de `LazyVerticalGrid` pour afficher la liste des recettes). |
| **Affichage d'une image pour chaque recette** | 2 pts |  Fait | **Fichier :** `components/RecipeCard.kt` (Image affichée avec `AsyncImage`). |
| **Chargement de la page suivante en arrivant en bas de la liste** | 4 pts |  Fait | **Fichier :** `screens/RecipeListScreen.kt`, **Méthode :** `LaunchedEffect(listState.layoutInfo.visibleItemsInfo)` (Détection du scroll et chargement des nouvelles pages). |
| **Accès aux détails d'une recette en appuyant dessus** | 4 pts | Fait | **Fichier :** `screens/RecipeListScreen.kt` (Navigation vers l’écran de détails). |
| **Filtrage des recettes via une barre de recherche** | 4 pts |  Fait | **Fichier :** `screens/RecipeListScreen.kt`, **Méthode :** `filteredRecipes` (Recherche avec `contains(searchQuery, ignoreCase = true)`). |
| **Filtrage par catégories via des boutons** | 3 pts |  Fait | **Fichier :** `components/RecipeFilters.kt` (Boutons interactifs avec `onClick`). |
| **Mode hors-ligne : accès aux recettes après consultation initiale** | 8 pts |  Fait | **Fichier :** `db/RecipeDatabase.kt`, **Méthode :** `getDatabase(context)` (Stockage des recettes avec Room). |
| **Mise à jour régulière des données locales** | 4 pts |  Fait | **Fichier :** `repositories/RecipeRepository.kt`, **Méthode :** `fetchRecipes()` (Mise à jour des recettes locales après récupération API). |
| **Architecture segmentée en couches** | 5 pts |  Fait | **Structure :** Séparation en `screens/`, `repositories/`, `db/`, `models/`, `components/` (Architecture propre et maintenable). |

 **Total obtenu : 40 / 40 points** 

---

##  Technologies Utilisées
- **Langage** : Kotlin
- **Interface utilisateur** : Jetpack Compose
- **Base de données locale** : Room Database
- **Récupération des données** : Ktor (API REST)
- **Gestion de la navigation** : Navigation Compose
- **Coroutines** : pour la gestion des appels asynchrones
- **Flow** : pour la mise à jour des données en temps réel

---

##  Installation et Exécution

###  Prérequis
- Android Studio installé
- Utiliser l’émulateur d’Android Studio ou un appareil Android connecté

###  Exécuter l'application
1. Cloner le projet :
   ```bash
   git clone https://github.com/zakaribel-dev/recipes.git
