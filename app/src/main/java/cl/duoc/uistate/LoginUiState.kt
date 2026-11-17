package cl.duoc.uistate

import cl.duoc.data.entity.UsuarioEntity

/**
 * Estado que almacena los campos y errores de la pantalla de inicio de sesión.
 */
data class LoginUiState(
    val rut: String = "",
    val contrasena: String = "",
    val rutError: String? = null,
    val contrasenaError: String? = null,
    val recordarUsuario: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val usuarioLogueado: UsuarioEntity? = null
)
