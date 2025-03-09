#  Recipe Explorer - Cooking Recipes Application

## Project Overview
Recipe Explorer is a mobile application developed in **Kotlin with Jetpack Compose**, allowing users to **browse, search, and filter cooking recipes**.

It uses a **local database (Room) with API synchronization**, enabling **offline functionality** if recipes have been accessed at least once.

---

##  Main Features

### Display and Navigation
- **Splash screen** with a customized logo.
- **List of available recipes**, displaying **title, image, and author**.
- **Access recipe details** by tapping on a recipe.
- **Recipe images displayed** directly in the list.

### Advanced Search and Filtering
- **Search bar** allowing filtering of recipes by **title**.
- **Category filters** (e.g., **Meat, Cakes, All**) via interactive buttons.

### Dynamic Loading and Pagination
- **Automatic loading of the next pages** when the user reaches the bottom of the list.
- **Smooth scrolling experience** with optimized scroll management.

### Data Management and Offline Mode
- **Recipes stored locally** using **Room Database**.
- **Regular updates** and synchronization with the API.
- **Offline mode**: previously accessed recipes remain available even without an internet connection.

### Organized Layered Architecture
The application is structured into **multiple modules**, ensuring **maintainability** and **scalability**:

components 
db
models
repositories
screens
MainActivity.kt

---

##  Technologies Used
- **Language**: Kotlin
- **User Interface**: Jetpack Compose
- **Local Database**: Room Database
- **Data Retrieval**: Ktor (REST API)
- **Navigation Management**: Navigation Compose
- **Coroutines**: for asynchronous operations
- **Flow**: for real-time data updates

---

##  Installation and Execution

###  Prerequisites
- Android Studio installed
- Use the Android Studio emulator or a connected Android device

###  Running the Application
1. Clone the project:
   ```bash
   git clone https://github.com/zakaribel-dev/recipes.git
