package com.example.recipes.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

//  Converters : Classe qui permet de convertir des types complexes en types simples pour Room
class Converters {

    //  Convertit une liste de chaînes (`List<String>`) en une chaîne de texte (`String`)
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Json.encodeToString(list) //  Convertit la liste en **chaîne JSON**
    }

    //  Convertit une chaîne de texte (`String`) en une liste de chaînes (`List<String>`)
    @TypeConverter
    fun fromStringToList(data: String): List<String> {
        return Json.decodeFromString(data) //  Convertit la chaîne JSON en **liste**
    }
}
