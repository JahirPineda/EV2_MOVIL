package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.UsuarioEntity
import cl.duoc.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

/**
 * ViewModel para registrar nuevos usuarios (Docentes o Estudiantes) en la base de datos Room.
 */
class RegisterViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Métodos para actualizar cada campo cuando el usuario escribe
    fun onRutChanged(rut: String) {
        _uiState.value = _uiState.value.copy(rut = rut, errorMessage = null)
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

    fun onRolChanged(rol: String) {
        _uiState.value = _uiState.value.copy(rol = rol)
    }

    fun onCursoChanged(curso: String) {
        _uiState.value = _uiState.value.copy(cursoAsignado = curso)
    }

    fun onAsignaturaChanged(asignatura: String) {
        _uiState.value = _uiState.value.copy(asignaturaAsignada = asignatura)
    }

    // Guarda el nuevo usuario en Room
    fun registrarUsuario(onSuccess: () -> Unit) {
        val estadoActual = _uiState.value

        // Validación básica de campos vacíos
        if (estadoActual.rut.isBlank() || estadoActual.nombre.isBlank() ||
            estadoActual.correo.isBlank() || estadoActual.contrasena.isBlank()) {
            _uiState.value = estadoActual.copy(errorMessage = "Por favor complete todos los campos obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = estadoActual.copy(isLoading = true, errorMessage = null)
            try {
                // Comprobamos si el RUT ya está en uso
                val usuarioExistente = repository.obtenerUsuarioPorId(estadoActual.rut.trim())
                if (usuarioExistente != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Ya existe un usuario registrado con este RUT"
                    )
                    return@launch
                }

                // Creamos la nueva entidad de usuario
                val nuevoUsuario = UsuarioEntity(
                    id = estadoActual.rut.trim(),
                    rut = estadoActual.rut.trim(),
                    nombre = estadoActual.nombre.trim(),
                    correo = estadoActual.correo.trim(),
                    contrasena = estadoActual.contrasena.trim(),
                    rol = estadoActual.rol,
                    cursoAsignado = estadoActual.cursoAsignado,
                    asignaturaAsignada = if (estadoActual.rol == "DOCENTE") estadoActual.asignaturaAsignada else null
                )

                // Guardamos en Room
                repository.insertarUsuario(nuevoUsuario)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = "¡Usuario registrado con éxito! Ya puedes iniciar sesión."
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al registrarse: ${e.localizedMessage}"
                )
            }
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(repository) as T
        }
    }
}
