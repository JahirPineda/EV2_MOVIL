package cl.duoc.uistate

import cl.duoc.data.entity.UsuarioEntity

/**
 * Modelo de datos para mostrar a cada estudiante con su cantidad de aportes.
 */
data class EstudianteSeguimientoItem(
    val usuario: UsuarioEntity,
    val cantidadRegistros: Int,
    val haParticipado: Boolean
)

/**
 * Estado de la pantalla de seguimiento del curso.
 */
data class SeguimientoUiState(
    val totalEstudiantes: Int = 0,
    val estudiantesParticipando: Int = 0,
    val alumnos: List<EstudianteSeguimientoItem> = emptyList()
)
