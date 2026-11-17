package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.BitacoraEntity
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que permite al docente revisar los aportes de los estudiantes para una bitácora.
 */
class RevisionDocenteViewModel(
    private val repository: AppRepository,
    private val bitacoraId: Long
) : ViewModel() {

    // Guarda los datos de la bitácora que se está revisando
    private val _bitacora = MutableStateFlow<BitacoraEntity?>(null)
    val bitacora: StateFlow<BitacoraEntity?> = _bitacora.asStateFlow()

    // Guarda los registros de los estudiantes enviados a esta bitácora
    private val _registros = MutableStateFlow<List<RegistroEntity>>(emptyList())
    val registros: StateFlow<List<RegistroEntity>> = _registros.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            // Obtenemos la información de la bitácora
            _bitacora.value = repository.obtenerBitacoraPorId(bitacoraId)
        }

        viewModelScope.launch {
            // Escuchamos la lista de registros de los estudiantes
            repository.obtenerRegistrosPorBitacora(bitacoraId).collect { lista ->
                _registros.value = lista
            }
        }
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(
        private val repository: AppRepository,
        private val bitacoraId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RevisionDocenteViewModel(repository, bitacoraId) as T
        }
    }
}
