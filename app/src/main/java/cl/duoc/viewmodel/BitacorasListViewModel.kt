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
 * ViewModel que gestiona la lista de todas las bitácoras registradas en Room.
 */
class BitacorasListViewModel(private val repository: AppRepository) : ViewModel() {

    // Lista observable de bitácoras
    private val _bitacoras = MutableStateFlow<List<BitacoraEntity>>(emptyList())
    val bitacoras: StateFlow<List<BitacoraEntity>> = _bitacoras.asStateFlow()

    init {
        // Al crearse el ViewModel, cargamos las bitácoras desde la base de datos
        cargarBitacoras()
    }

    private fun cargarBitacoras() {
        viewModelScope.launch {
            // Escuchamos los cambios en Room y actualizamos la lista
            repository.obtenerTodasLasBitacoras().collect { lista ->
                _bitacoras.value = lista
            }
        }
    }

    // Factoría sencilla para crear este ViewModel pasándole el repositorio
    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BitacorasListViewModel(repository) as T
        }
    }
}
