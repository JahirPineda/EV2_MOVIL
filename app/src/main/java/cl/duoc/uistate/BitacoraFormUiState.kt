package cl.duoc.uistate

/**
 * Estado que almacena los campos del formulario de creación y edición de bitácoras.
 */
data class BitacoraFormUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val curso: String = "1.º básico A",
    val estado: String = "PUBLICADA", // "BORRADOR", "PUBLICADA", "CERRADA"
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val fechaError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)
