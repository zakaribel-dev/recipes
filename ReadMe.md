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
