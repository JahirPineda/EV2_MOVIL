package cl.duoc.uistate

/**
 * Estado que almacena los campos del formulario de registro de usuario.
 */
data class RegisterUiState(
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val rol: String = "ESTUDIANTE", // "DOCENTE" o "ESTUDIANTE"
    val cursoAsignado: String = "6.º básico A",
    val asignaturaAsignada: String = "Matemáticas",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)
