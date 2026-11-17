package cl.duoc.uistate

/**
 * Estado del formulario para registrar o editar un avance de aprendizaje.
 */
data class RegistroFormUiState(
    val fecha: String = "",
    val observacion: String = "",
    val fotoUri: String? = null,
    val esRevisado: Boolean = false,
    val fechaError: String? = null,
    val observacionError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)
