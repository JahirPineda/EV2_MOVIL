package cl.duoc.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.duoc.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/**
 * Objeto de Acceso a Datos (DAO) para la tabla de usuarios en Room.
 */
@Dao
interface UsuarioDao {

    // Valida credenciales comparando correo o RUT (con o sin formato) y la contraseña
    @Query("SELECT * FROM usuarios WHERE (correo = :correo OR id = :correo OR rut = :correo OR replace(replace(rut, '.', ''), '-', '') = replace(replace(:correo, '.', ''), '-', '')) AND contrasena = :pass LIMIT 1")
    suspend fun login(correo: String, pass: String): UsuarioEntity?

    // Busca un usuario por su RUT o ID
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: String): UsuarioEntity?

    // Obtiene la lista completa de usuarios
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<UsuarioEntity>>

    // Inserta un único usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    // Inserta una lista completa de usuarios iniciales
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>)

    // Actualiza datos de un usuario
    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    // Elimina un usuario
    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)

    // Cuenta cuántos usuarios existen en la base de datos
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int
}
