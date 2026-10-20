package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que permite al docente ver el detalle de un aporte y dejar una retroalimentación.
 */
class DetalleRegistroDocenteViewModel(
    private val repository: AppRepository,
    private val registroId: Long
) : ViewModel() {

    private val _registro = MutableStateFlow<RegistroEntity?>(null)
    val registro: StateFlow<RegistroEntity?> = _registro.asStateFlow()

    init {
        cargarRegistro()
    }

    // Busca el registro en la base de datos
    fun cargarRegistro() {
        viewModelScope.launch {
            _registro.value = repository.obtenerRegistroPorId(registroId)
        }
    }

    // Guarda el comentario del docente y actualiza el estado (ej: "REVISADO")
    fun actualizarRetroalimentacionYEstado(comentario: String, nuevoEstado: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.actualizarRetroalimentacionYEstadoRegistro(registroId, nuevoEstado, comentario)
            // Recargamos el registro actualizado
            cargarRegistro()
            onComplete()
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(
        private val repository: AppRepository,
        private val registroId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetalleRegistroDocenteViewModel(repository, registroId) as T
        }
    }
}
