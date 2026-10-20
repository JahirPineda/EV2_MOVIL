package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.SessionManager
import cl.duoc.data.entity.UsuarioEntity
import cl.duoc.data.repository.AppRepository
import cl.duoc.util.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

/**
 * ViewModel que gestiona la modificación de la información del perfil del usuario (Docente o Estudiante).
 */
class PerfilViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    /**
     * Inicia el modo edición cargando los valores actuales del usuario.
     */
    fun iniciarEdicion(usuario: UsuarioEntity?) {
        if (usuario == null) return
        _uiState.value = _uiState.value.copy(
            isEditing = true,
            nombre = usuario.nombre,
            correo = usuario.correo,
            contrasena = usuario.contrasena,
            cursoAsignado = usuario.cursoAsignado ?: "",
            asignaturaAsignada = usuario.asignaturaAsignada ?: "",
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Cancela el modo edición y limpia mensajes.
     */
    fun cancelarEdicion() {
        _uiState.value = _uiState.value.copy(
            isEditing = false,
            errorMessage = null
        )
    }

    fun onNombreChanged(nombre: String) {
        _uiState.value = _uiState.value.copy(nombre = nombre, errorMessage = null)
    }

    fun onCorreoChanged(correo: String) {
        _uiState.value = _uiState.value.copy(correo = correo, errorMessage = null)
    }

    fun onContrasenaChanged(contrasena: String) {
        _uiState.value = _uiState.value.copy(contrasena = contrasena, errorMessage = null)
    }

    fun onCursoChanged(curso: String) {
        _uiState.value = _uiState.value.copy(cursoAsignado = curso, errorMessage = null)
    }

    fun onAsignaturaChanged(asignatura: String) {
        _uiState.value = _uiState.value.copy(asignaturaAsignada = asignatura, errorMessage = null)
    }

    /**
     * Valida y guarda los cambios del usuario manteniendo los tipos de datos intactos.
     */
    fun guardarCambios(usuarioActual: UsuarioEntity?) {
        if (usuarioActual == null) return

        val estado = _uiState.value
        val nombreTrim = estado.nombre.trim()
        val correoTrim = estado.correo.trim()
        val passTrim = estado.contrasena.trim()
        val cursoTrim = estado.cursoAsignado.trim()
        val asignaturaTrim = estado.asignaturaAsignada.trim()

        // Validaciones
        if (!ValidationUtils.esCampoNoVacio(nombreTrim)) {
            _uiState.value = estado.copy(errorMessage = "El nombre no puede estar vacío")
            return
        }

        if (!ValidationUtils.esCorreoValido(correoTrim)) {
            _uiState.value = estado.copy(errorMessage = "Ingrese un correo electrónico válido")
            return
        }

        if (!ValidationUtils.validarPassword(passTrim)) {
            _uiState.value = estado.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        val esDocente = usuarioActual.rol.equals("DOCENTE", ignoreCase = true)

        val usuarioActualizado = usuarioActual.copy(
            nombre = nombreTrim,
            correo = correoTrim,
            contrasena = passTrim,
            cursoAsignado = cursoTrim.ifBlank { null },
            asignaturaAsignada = if (esDocente) asignaturaTrim.ifBlank { null } else null
        )

        viewModelScope.launch {
            _uiState.value = estado.copy(isLoading = true, errorMessage = null)
            try {
                // Actualiza en Room Database
                repository.actualizarUsuario(usuarioActualizado)

                // Actualiza la sesión global
                SessionManager.iniciarSesion(usuarioActualizado)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEditing = false,
                    successMessage = "¡Información de perfil actualizada con éxito!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al actualizar perfil: ${e.localizedMessage}"
                )
            }
        }
    }

    fun limpiarMensajeExito() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PerfilViewModel(repository) as T
        }
    }
}
