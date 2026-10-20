package cl.duoc.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "bitacoras" en la base de datos Room.
 * Representa una bitácora mensual creada por un docente para un curso.
 */
@Entity(
    tableName = "bitacoras",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["docenteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("docenteId")]
)
data class BitacoraEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, // ID numérico autoincrementable
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val estado: String, // "BORRADOR", "PUBLICADA", "CERRADA"
    val docenteId: String // Llave foránea que apunta al RUT del docente
)
