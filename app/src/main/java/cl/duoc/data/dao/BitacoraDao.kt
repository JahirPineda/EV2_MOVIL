package cl.duoc.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.duoc.data.entity.BitacoraEntity
import kotlinx.coroutines.flow.Flow

/**
 * Objeto de Acceso a Datos (DAO) para la tabla de bitácoras en Room.
 * Define las operaciones SQL que se pueden realizar sobre las bitácoras.
 */
@Dao
interface BitacoraDao {

    // Obtiene las bitácoras creadas por un docente específico
    @Query("SELECT * FROM bitacoras WHERE docenteId = :docenteId")
    fun obtenerPorDocente(docenteId: String): Flow<List<BitacoraEntity>>

    // Obtiene bitácoras según su estado (ej: "PUBLICADA", "BORRADOR", "CERRADA")
    @Query("SELECT * FROM bitacoras WHERE estado = :estado")
    fun obtenerPorEstado(estado: String): Flow<List<BitacoraEntity>>

    // Obtiene todas las bitácoras de la base de datos
    @Query("SELECT * FROM bitacoras")
    fun obtenerTodas(): Flow<List<BitacoraEntity>>

    // Busca una bitácora por su ID único
    @Query("SELECT * FROM bitacoras WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): BitacoraEntity?

    // Actualiza solo el estado de la bitácora
    @Query("UPDATE bitacoras SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Long, estado: String)

    // Inserta una nueva bitácora (si ya existe, la reemplaza)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(bitacora: BitacoraEntity): Long

    // Actualiza todos los campos de una bitácora
    @Update
    suspend fun actualizar(bitacora: BitacoraEntity)

    // Elimina una bitácora
    @Delete
    suspend fun eliminar(bitacora: BitacoraEntity)
}
