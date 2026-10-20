package cl.duoc.model

/**
 * Modelos de datos simples (POJOs/Data Classes) utilizados para representar la información
 * de bitácoras, registros, seguimientos y recordatorios en la aplicación.
 */

// Representa una bitácora escolar
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

// Representa el registro o aporte enviado por un estudiante
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

// Representa el estado de participación de un estudiante para el seguimiento docente
data class EstudianteSeguimiento(
    val nombre: String,
    val curso: String,
    val registros: Int,
    val participo: Boolean
)

// Representa un aviso o recordatorio de tareas pendientes
data class Recordatorio(
    val id: String,
    val titulo: String,
    val fecha: String,
    val descripcion: String,
    val completado: Boolean = false
)
