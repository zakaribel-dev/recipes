
# 📱 Application Android Kotlin - Recettes de cuisine 🍽️

Cette application Android développée en **Kotlin** utilise **Jetpack Compose**, **Room** et **Ktor** pour afficher des recettes de cuisine récupérées depuis une API REST externe. Les recettes sont stockées localement dans une base de données **SQLite**, permettant ainsi une utilisation hors-ligne.

---



## 📂 Structure du projet

components 
db
models
repositories
screens
MainActivity.kt

## 🗃️ Gestion des données avec Room

L'application utilise **Room Database** pour stocker les recettes en local.

- **RecipeDatabase.kt** : Singleton pour la base de données.
- **RecipeDao.kt** : Interface contenant les requêtes SQL (`getAllRecipes()`, `insertRecipes()`, `clearRecipes()`).
- **Converters.kt** : Gestion des listes d'ingrédients sous forme de **JSON** dans Room.
- **Recipe.kt** : Définit la structure des recettes stockées en local..

---

## 🔄 Repository et API (Ktor)

Le **RecipeRepository** assure la gestion des données :

1. 🔍 **Recherche et filtrage des recettes**
2. 🌐 **Récupération des recettes via l'API externe**
3. 💾 **Stockage local dans Room pour une utilisation hors-ligne**
4. 🔄 **Mise à jour régulière des données**

API utilisée :
- **Food2Fork API**
- 🔗 `https://food2fork.ca/api/recipe/search/`

---

## 🎨 Interface utilisateur (Jetpack Compose)

### 📋 **Liste des recettes (RecipeListScreen)**
✅ Affichage des recettes sous forme de **grille** (LazyVerticalGrid)  
✅ **Pagination automatique** (scroll infini)  
✅ **Barre de recherche** pour filtrer par titre  
✅ **Filtres par catégorie** avec boutons interactifs

### 📖 **Détails d'une recette (RecipeDetailScreen)**
✅ Affichage **dynamique** avec image haute définition  
✅ Liste des **ingrédients** et instructions  
✅ Navigation fluide avec retour vers la liste

---

## ⚙️ Fonctionnement hors-ligne

✅ **Sauvegarde automatique** des recettes consultées  
✅ **Accès aux recettes même sans connexion Internet**  
✅ **Mise à jour automatique** des données dès qu'Internet est disponible

---


### 1️⃣ Cloner le projet :
```bash
git clone https://github.com/zakaribel-dev/recipes.git
