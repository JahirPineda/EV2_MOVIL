package cl.duoc.uistate

/**
 * Estado UI para la pantalla de Perfil de usuario.
 */
data class PerfilUiState(
    val isEditing: Boolean = false,
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val cursoAsignado: String = "",
    val asignaturaAsignada: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
