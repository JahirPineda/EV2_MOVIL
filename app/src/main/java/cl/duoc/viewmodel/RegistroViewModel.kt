package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.repository.AppRepository
import cl.duoc.util.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

/**
 * ViewModel que permite al estudiante crear o editar un registro de aprendizaje con evidencia fotográfica.
 */
class RegistroViewModel(
    private val repository: AppRepository,
    private val registroId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroFormUiState())
    val uiState: StateFlow<RegistroFormUiState> = _uiState.asStateFlow()

    init {
        // Si estamos editando un registro previo, cargamos sus valores
        if (registroId != null && registroId > 0L) {
            viewModelScope.launch {
                val registro = repository.obtenerRegistroPorId(registroId)
                if (registro != null) {
                    _uiState.value = _uiState.value.copy(
                        fecha = registro.fecha,
                        observacion = registro.observacion,
                        fotoUri = registro.fotoUri,
                        esRevisado = registro.estado.equals("REVISADO", ignoreCase = true)
                    )
                }
            }
        }
    }

    // Funciones para actualizar los datos al escribir
    fun onFechaChanged(fecha: String) {
        if (_uiState.value.esRevisado) return
        _uiState.value = _uiState.value.copy(fecha = fecha, fechaError = null, errorMessage = null)
    }

    fun onObservacionChanged(observacion: String) {
        if (_uiState.value.esRevisado) return
        _uiState.value = _uiState.value.copy(observacion = observacion, observacionError = null, errorMessage = null)
    }

    // Actualiza la URI de la foto seleccionada (Cámara o Galería)
    fun onFotoUriChanged(uri: String?) {
        if (_uiState.value.esRevisado) return
        _uiState.value = _uiState.value.copy(fotoUri = uri)
    }

    // Valida que la fecha y la observación tengan formato correcto
    private fun validar(): Boolean {
        val estadoActual = _uiState.value
        var esValido = true
        var errorFecha: String? = null
        var errorObservacion: String? = null

        if (!ValidationUtils.validarFechaDdMMyyyy(estadoActual.fecha)) {
            errorFecha = "Fecha inválida (formato requerido: dd/MM/yyyy)"
            esValido = false
        }
        if (!ValidationUtils.validarLongitudMinima(estadoActual.observacion, 10)) {
            errorObservacion = "La observación debe tener al menos 10 caracteres"
            esValido = false
        }

        _uiState.value = estadoActual.copy(
            fechaError = errorFecha,
            observacionError = errorObservacion
        )
        return esValido
    }

    // Guarda o actualiza el registro en Room
    fun guardarOActualizarRegistro(bitacoraId: Long, estudianteId: String, onComplete: () -> Unit) {
        val estadoActual = _uiState.value

        // Si ya está revisado por el profesor, está bloqueado contra modificaciones
        if (estadoActual.esRevisado) {
            _uiState.value = estadoActual.copy(
                errorMessage = "Este registro ya ha sido marcado como 'REVISADO' por el docente y no puede ser modificado."
            )
            return
        }

        if (!validar()) return

        viewModelScope.launch {
            _uiState.value = estadoActual.copy(isLoading = true, errorMessage = null)
            try {
                if (registroId == null || registroId <= 0L) {
                    // Modo Creación: Nuevo registro de aprendizaje
                    val nuevoRegistro = RegistroEntity(
                        bitacoraId = bitacoraId,
                        estudianteId = estudianteId,
                        observacion = estadoActual.observacion.trim(),
                        fotoUri = estadoActual.fotoUri,
                        fecha = estadoActual.fecha.trim(),
                        estado = "PENDIENTE"
                    )
                    repository.insertarRegistro(nuevoRegistro)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "¡Registro enviado con éxito!"
                    )
                } else {
                    // Modo Edición: Actualizar registro existente
                    val registroPrevio = repository.obtenerRegistroPorId(registroId)
                    val registroActualizado = RegistroEntity(
                        id = registroId,
                        bitacoraId = registroPrevio?.bitacoraId ?: bitacoraId,
                        estudianteId = registroPrevio?.estudianteId ?: estudianteId,
                        observacion = estadoActual.observacion.trim(),
                        fotoUri = estadoActual.fotoUri,
                        fecha = estadoActual.fecha.trim(),
                        estado = registroPrevio?.estado ?: "PENDIENTE",
                        retroalimentacionDocente = registroPrevio?.retroalimentacionDocente
                    )
                    repository.actualizarRegistro(registroActualizado)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "¡Registro actualizado con éxito!"
                    )
                }
                onComplete()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al guardar registro: ${e.localizedMessage}"
                )
            }
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(
        private val repository: AppRepository,
        private val registroId: Long? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegistroViewModel(repository, registroId) as T
        }
    }
}
