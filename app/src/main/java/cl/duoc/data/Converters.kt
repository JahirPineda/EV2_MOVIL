package cl.duoc.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Conversor de tipos para Room (opcional).
 * Permite transformar listas de texto a formato JSON y viceversa si se requiere almacenar listas.
 */
class Converters {
    private val gson = Gson()

    // Convierte una lista de Strings a un texto JSON
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    // Convierte un texto JSON de vuelta a una lista de Strings
    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
