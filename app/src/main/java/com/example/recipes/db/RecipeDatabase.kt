package com.example.recipes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipes.models.Recipe

//  Annotation @Database : Définit la base de données Room
// - `entities = [Recipe::class]` → Spécifie les entités stockées dans la base (ici, `Recipe`).
// - `version = 1` → Version de la base de données (utile pour les migrations).
// - `exportSchema = false` → Empêche Room de générer un fichier de schéma JSON (évite de stocker l’historique des migrations).
@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // 🔥 Permet d'utiliser des `TypeConverters` pour convertir des types complexes
abstract class RecipeDatabase : RoomDatabase() {

    //  Définition du DAO (Data Access Object)
    // - Fournit l'accès aux opérations de base de données.
    abstract fun recipeDao(): RecipeDao

    companion object {
        // Singleton : S'assure qu'il n'existe **qu'une seule instance** de la base de données dans l'application.
        private var INSTANCE: RecipeDatabase? = null

        //  Méthode pour obtenir l'instance unique de la base de données
        fun getDatabase(context: Context): RecipeDatabase {
            //  Vérification : Si aucune instance n’existe encore, on en crée une.
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,  //  Utilisation du `applicationContext` pour éviter les fuites mémoire
                    RecipeDatabase::class.java, //  Référence à la classe de la base
                    "recipe_database" // Nom du fichier de la base de données stocké sur l'appareil
                )
                    .fallbackToDestructiveMigration() // Si la structure de la base change, elle sera supprimée et recréée (⚠ Perte de données)
                    .build()
            }
            //  Retourne l'instance unique de la base
            return INSTANCE as RecipeDatabase
        }
    }
}
