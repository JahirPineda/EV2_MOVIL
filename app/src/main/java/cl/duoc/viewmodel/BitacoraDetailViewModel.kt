package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.BitacoraEntity
import cl.duoc.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para ver los detalles de una bitácora y permitir cerrarla.
 */
class BitacoraDetailViewModel(
    private val repository: AppRepository,
    private val bitacoraId: Long
) : ViewModel() {

    private val _bitacora = MutableStateFlow<BitacoraEntity?>(null)
    val bitacora: StateFlow<BitacoraEntity?> = _bitacora.asStateFlow()

    init {
        cargarBitacora()
    }

    // Busca la bitácora en la base de datos por su ID
    fun cargarBitacora() {
        viewModelScope.launch {
            _bitacora.value = repository.obtenerBitacoraPorId(bitacoraId)
        }
    }

    // Actualiza el estado de la bitácora (por ejemplo, a "CERRADA")
    fun actualizarEstado(nuevoEstado: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.actualizarEstadoBitacora(bitacoraId, nuevoEstado)
            // Recargamos los datos para reflejar el cambio en pantalla
            cargarBitacora()
            onComplete()
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(
        private val repository: AppRepository,
        private val bitacoraId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BitacoraDetailViewModel(repository, bitacoraId) as T
        }
    }
}
