package cl.duoc.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.duoc.data.entity.RegistroEntity
import kotlinx.coroutines.flow.Flow

/**
 * Objeto de Acceso a Datos (DAO) para los registros de aprendizaje de los alumnos en Room.
 */
@Dao
interface RegistroDao {

    // Obtiene los aportes de un estudiante para una bitácora en particular
    @Query("SELECT * FROM registros WHERE bitacoraId = :bitacoraId AND estudianteId = :estudianteId")
    fun obtenerPorEstudianteYBitacora(bitacoraId: Long, estudianteId: String): Flow<List<RegistroEntity>>

    // Obtiene todos los aportes de todos los estudiantes para una bitácora
    @Query("SELECT * FROM registros WHERE bitacoraId = :bitacoraId")
    fun obtenerPorBitacora(bitacoraId: Long): Flow<List<RegistroEntity>>

    // Obtiene el historial completo de un estudiante
    @Query("SELECT * FROM registros WHERE estudianteId = :estudianteId")
    fun obtenerPorEstudiante(estudianteId: String): Flow<List<RegistroEntity>>

    // Obtiene todos los registros guardados en la aplicación
    @Query("SELECT * FROM registros")
    fun obtenerTodos(): Flow<List<RegistroEntity>>

    // Busca un registro específico por su ID
    @Query("SELECT * FROM registros WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): RegistroEntity?

    // Actualiza el estado y el comentario de retroalimentación dejado por el docente
    @Query("UPDATE registros SET estado = :estado, retroalimentacionDocente = :retroalimentacion WHERE id = :id")
    suspend fun actualizarRetroalimentacionYEstado(id: Long, estado: String, retroalimentacion: String?)

    // Inserta un nuevo registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(registro: RegistroEntity): Long

    // Actualiza un registro existente
    @Update
    suspend fun actualizar(registro: RegistroEntity)

    // Elimina un registro
    @Delete
    suspend fun eliminar(registro: RegistroEntity)
}
