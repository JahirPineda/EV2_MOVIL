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
 * ViewModel que consulta y entrega el historial de registros de un estudiante.
 */
class HistorialRegistrosViewModel(
    private val repository: AppRepository,
    private val estudianteId: String
) : ViewModel() {

    // Lista observable de registros del estudiante
    private val _registros = MutableStateFlow<List<RegistroEntity>>(emptyList())
    val registros: StateFlow<List<RegistroEntity>> = _registros.asStateFlow()

    init {
        cargarRegistros()
    }

    private fun cargarRegistros() {
        viewModelScope.launch {
            // Buscamos los registros asociados al ID del estudiante en Room
            repository.obtenerRegistrosPorEstudiante(estudianteId).collect { lista ->
                _registros.value = lista
            }
        }
    }

    // Factoría sencilla para crear este ViewModel con el ID del estudiante
    class Factory(
        private val repository: AppRepository,
        private val estudianteId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistorialRegistrosViewModel(repository, estudianteId) as T
        }
    }
}
