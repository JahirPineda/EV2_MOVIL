package cl.duoc.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
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

/**
 * ViewModel que gestiona la autenticación de usuarios y el guardado de preferencias.
 */
class LoginViewModel(
    application: Application,
    private val repository: AppRepository
) : AndroidViewModel(application) {

    // SharedPreferences para recordar el RUT si el usuario marca la casilla
    private val prefs = application.getSharedPreferences("bitacora_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        // Al iniciar, revisamos si había un RUT guardado previamente
        val rutGuardado = prefs.getString("saved_rut", "") ?: ""
        val recordar = prefs.getBoolean("remember_user", false)
        if (recordar && rutGuardado.isNotBlank()) {
            _uiState.value = _uiState.value.copy(rut = rutGuardado, recordarUsuario = true)
        }
    }

    // Actualiza el campo RUT
    fun onRutChanged(rut: String) {
        _uiState.value = _uiState.value.copy(rut = rut, rutError = null, errorMessage = null)
    }

    // Actualiza el campo contraseña
    fun onContrasenaChanged(contrasena: String) {
        _uiState.value = _uiState.value.copy(contrasena = contrasena, contrasenaError = null, errorMessage = null)
    }

    // Actualiza la casilla "Recordar usuario"
    fun onRecordarUsuarioChanged(recordar: Boolean) {
        _uiState.value = _uiState.value.copy(recordarUsuario = recordar)
    }

    // Procesa el inicio de sesión
    fun login(onSuccess: (String) -> Unit) {
        val estadoActual = _uiState.value
        var esValido = true
        var errorRut: String? = null
        var errorPass: String? = null

        // 1. Validar RUT
        if (estadoActual.rut.isBlank()) {
            errorRut = "El RUT es obligatorio"
            esValido = false
        } else if (!ValidationUtils.validarRutChileno(estadoActual.rut)) {
            errorRut = "RUT inválido (formato chileno con dígito verificador)"
            esValido = false
        }

        // 2. Validar Contraseña
        if (estadoActual.contrasena.isBlank()) {
            errorPass = "La contraseña es obligatoria"
            esValido = false
        } else if (!ValidationUtils.validarPassword(estadoActual.contrasena)) {
            errorPass = "La contraseña debe tener al menos 6 caracteres"
            esValido = false
        }

        // Si hay errores de formato, los mostramos y no continuamos
        if (!esValido) {
            _uiState.value = estadoActual.copy(rutError = errorRut, contrasenaError = errorPass)
            return
        }

        // 3. Consultar a la base de datos Room
        viewModelScope.launch {
            _uiState.value = estadoActual.copy(isLoading = true, errorMessage = null)
            try {
                val inputRut = estadoActual.rut.trim()
                val pass = estadoActual.contrasena.trim()
                val usuario = repository.login(inputRut, pass)

                if (usuario != null) {
                    // Guardamos el usuario en la sesión global
                    SessionManager.iniciarSesion(usuario)

                    // Guardamos o borramos de SharedPreferences según la preferencia
                    val editor = prefs.edit()
                    if (estadoActual.recordarUsuario) {
                        editor.putString("saved_rut", inputRut)
                        editor.putBoolean("remember_user", true)
                    } else {
                        editor.remove("saved_rut")
                        editor.putBoolean("remember_user", false)
                    }
                    editor.apply()

                    _uiState.value = _uiState.value.copy(isLoading = false, usuarioLogueado = usuario)
                    // Navegamos pasando el rol ("DOCENTE" o "ESTUDIANTE")
                    onSuccess(usuario.rol)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Credenciales incorrectas o usuario no encontrado"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al iniciar sesión: ${e.localizedMessage}"
                )
            }
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(
        private val application: Application,
        private val repository: AppRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(application, repository) as T
        }
    }
}
