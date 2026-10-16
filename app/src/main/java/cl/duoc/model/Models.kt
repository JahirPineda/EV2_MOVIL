package cl.duoc.model

data class Bitacora(
    val id: String,
    val titulo: String,
    val asignatura: String,
    val curso: String,
    val mes: String,
    val anio: String = "2026",
    val estado: String = "Publicada", // Publicada, Borrador, Cerrada
    val docente: String = "María González",
    val proposito: String = "",
    val instrucciones: String = "",
    val preguntasOrientadoras: List<String> = emptyList()
)

data class Registro(
    val id: String,
    val bitacoraId: String,
    val bitacoraTitulo: String,
    val estudiante: String,
    val curso: String,
    val asignatura: String,
    val fecha: String,
    val contenidos: String,
    val actividades: String,
    val aprendizajes: String,
    val dificultades: String,
    val evidencias: List<String> = emptyList(),
    val estado: String = "Pendiente", // Pendiente, Revisado, Borrador
    val comentarioDocente: String = ""
)

data class EstudianteSeguimiento(
    val nombre: String,
    val curso: String,
    val registros: Int,
    val participo: Boolean
)

data class Recordatorio(
    val id: String,
    val titulo: String,
    val fecha: String,
    val descripcion: String,
    val completado: Boolean = false
)
