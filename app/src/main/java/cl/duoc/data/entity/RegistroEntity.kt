package cl.duoc.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "registros" en la base de datos Room.
 * Almacena los aportes de aprendizaje que envían los estudiantes a cada bitácora.
 */
@Entity(
    tableName = "registros",
    foreignKeys = [
        ForeignKey(
            entity = BitacoraEntity::class,
            parentColumns = ["id"],
            childColumns = ["bitacoraId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["estudianteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bitacoraId"), Index("estudianteId")]
)
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val bitacoraId: Long, // ID de la bitácora a la que pertenece este registro
    val estudianteId: String, // RUT del estudiante que hizo el registro
    val observacion: String, // Texto con lo aprendido, avances o dudas
    val fotoUri: String?, // Ruta o URI de la foto adjunta (opcional)
    val fecha: String,
    val estado: String = "PENDIENTE", // "PENDIENTE" o "REVISADO"
    val retroalimentacionDocente: String? = null // Comentario del profesor
)
