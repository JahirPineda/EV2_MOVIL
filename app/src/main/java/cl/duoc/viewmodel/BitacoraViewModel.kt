package cl.duoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.BitacoraEntity
import cl.duoc.data.repository.AppRepository
import cl.duoc.util.NotificationHelper
import cl.duoc.util.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado que almacena los campos del formulario de creación y edición de bitácoras.
 */
data class BitacoraFormUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val estado: String = "PUBLICADA", // "BORRADOR", "PUBLICADA", "CERRADA"
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val fechaError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)

/**
 * ViewModel que permite al docente crear una nueva bitácora o editar una existente.
 */
class BitacoraViewModel(
    application: Application,
    private val repository: AppRepository,
    private val bitacoraId: Long? = null
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(BitacoraFormUiState())
    val uiState: StateFlow<BitacoraFormUiState> = _uiState.asStateFlow()

    init {
        // Si estamos editando una bitácora existente, cargamos sus datos actuales
        if (bitacoraId != null && bitacoraId > 0L) {
            viewModelScope.launch {
                val bitacora = repository.obtenerBitacoraPorId(bitacoraId)
                if (bitacora != null) {
                    _uiState.value = _uiState.value.copy(
                        titulo = bitacora.titulo,
                        descripcion = bitacora.descripcion,
                        fecha = bitacora.fecha,
                        estado = bitacora.estado
                    )
                }
            }
        }
    }

    // Funciones para actualizar los datos al escribir
    fun onTituloChanged(titulo: String) {
        _uiState.value = _uiState.value.copy(titulo = titulo, tituloError = null, errorMessage = null)
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.value = _uiState.value.copy(descripcion = descripcion, descripcionError = null, errorMessage = null)
    }

    fun onFechaChanged(fecha: String) {
        _uiState.value = _uiState.value.copy(fecha = fecha, fechaError = null, errorMessage = null)
    }

    fun onEstadoChanged(estado: String) {
        _uiState.value = _uiState.value.copy(estado = estado)
    }

    // Valida que el formulario cumpla los requisitos mínimos
    private fun validar(): Boolean {
        val estadoActual = _uiState.value
        var esValido = true
        var errorTitulo: String? = null
        var errorDescripcion: String? = null
        var errorFecha: String? = null

        if (!ValidationUtils.validarLongitudMinima(estadoActual.titulo, 5)) {
            errorTitulo = "El título debe tener al menos 5 caracteres"
            esValido = false
        }
        if (!ValidationUtils.validarLongitudMinima(estadoActual.descripcion, 10)) {
            errorDescripcion = "La descripción debe tener al menos 10 caracteres"
            esValido = false
        }
        if (!ValidationUtils.validarFechaDdMMyyyy(estadoActual.fecha)) {
            errorFecha = "Fecha inválida (formato requerido: dd/MM/yyyy)"
            esValido = false
        }

        _uiState.value = estadoActual.copy(
            tituloError = errorTitulo,
            descripcionError = errorDescripcion,
            fechaError = errorFecha
        )
        return esValido
    }

    // Guarda o actualiza la bitácora en la base de datos Room
    fun guardarBitacora(docenteId: String, onComplete: () -> Unit) {
        if (!validar()) return

        val estadoActual = _uiState.value
        viewModelScope.launch {
            _uiState.value = estadoActual.copy(isLoading = true, errorMessage = null)
            try {
                if (bitacoraId == null || bitacoraId <= 0L) {
                    // Modo Creación: Guardamos nueva bitácora
                    val nuevaBitacora = BitacoraEntity(
                        titulo = estadoActual.titulo.trim(),
                        descripcion = estadoActual.descripcion.trim(),
                        fecha = estadoActual.fecha.trim(),
                        estado = estadoActual.estado,
                        docenteId = docenteId
                    )
                    repository.insertarBitacora(nuevaBitacora)

                    // Enviamos notificación al celular
                    NotificationHelper.mostrarNotificacion(
                        getApplication(),
                        "Nueva Bitácora Creada",
                        "Se ha guardado la bitácora '${estadoActual.titulo}' (${estadoActual.estado})"
                    )
                } else {
                    // Modo Edición: Actualizamos la bitácora existente
                    val bitacoraActualizada = BitacoraEntity(
                        id = bitacoraId,
                        titulo = estadoActual.titulo.trim(),
                        descripcion = estadoActual.descripcion.trim(),
                        fecha = estadoActual.fecha.trim(),
                        estado = estadoActual.estado,
                        docenteId = docenteId
                    )
                    repository.actualizarBitacora(bitacoraActualizada)

                    // Notificación de actualización
                    NotificationHelper.mostrarNotificacion(
                        getApplication(),
                        "Bitácora Modificada",
                        "Se ha actualizado la bitácora '${estadoActual.titulo}' (${estadoActual.estado})"
                    )
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = "Bitácora guardada exitosamente"
                )
                onComplete()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al guardar bitácora: ${e.localizedMessage}"
                )
            }
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(
        private val application: Application,
        private val repository: AppRepository,
        private val bitacoraId: Long? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BitacoraViewModel(application, repository, bitacoraId) as T
        }
    }
}
