package cl.duoc.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "usuarios" en la base de datos Room.
 * Almacena la información de profesores y alumnos para el login y gestión de roles.
 */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String, // Usamos el RUT como identificador único
    val rut: String,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: String, // "DOCENTE" o "ESTUDIANTE"
    val cursoAsignado: String?,
    val asignaturaAsignada: String?
)
