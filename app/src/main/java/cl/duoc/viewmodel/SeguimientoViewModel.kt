package cl.duoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.entity.UsuarioEntity
import cl.duoc.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Modelo de datos para mostrar a cada estudiante con su cantidad de aportes.
 */
data class EstudianteSeguimientoItem(
    val usuario: UsuarioEntity,
    val cantidadRegistros: Int,
    val haParticipado: Boolean
)

/**
 * Estado de la pantalla de seguimiento del curso.
 */
data class SeguimientoUiState(
    val totalEstudiantes: Int = 0,
    val estudiantesParticipando: Int = 0,
    val alumnos: List<EstudianteSeguimientoItem> = emptyList()
)

/**
 * ViewModel para calcular las métricas de participación de los estudiantes del curso.
 */
class SeguimientoViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SeguimientoUiState())
    val uiState: StateFlow<SeguimientoUiState> = _uiState.asStateFlow()

    init {
        cargarSeguimiento()
    }

    private fun cargarSeguimiento() {
        viewModelScope.launch {
            // Obtenemos los usuarios y los registros desde Room
            repository.obtenerTodosLosUsuarios().collect { usuarios ->
                repository.obtenerTodosLosRegistros().collect { registros ->
                    procesarDatos(usuarios, registros)
                }
            }
        }
    }

    // Calcula cuántos estudiantes han participado
    private fun procesarDatos(usuarios: List<UsuarioEntity>, registros: List<RegistroEntity>) {
        // Filtramos para considerar solo a los usuarios con rol ESTUDIANTE
        val listaEstudiantes = usuarios.filter { it.rol.equals("ESTUDIANTE", ignoreCase = true) }
        val resultadoAlumnos = mutableListOf<EstudianteSeguimientoItem>()

        // Para cada estudiante, contamos cuántos registros ha realizado
        for (estudiante in listaEstudiantes) {
            val registrosDelEstudiante = registros.filter { it.estudianteId == estudiante.id }
            val cantidad = registrosDelEstudiante.size
            val participo = cantidad > 0

            resultadoAlumnos.add(
                EstudianteSeguimientoItem(
                    usuario = estudiante,
                    cantidadRegistros = cantidad,
                    haParticipado = participo
                )
            )
        }

        val total = listaEstudiantes.size
        val participantes = resultadoAlumnos.count { it.haParticipado }

        // Actualizamos el estado de la pantalla
        _uiState.value = SeguimientoUiState(
            totalEstudiantes = total,
            estudiantesParticipando = participantes,
            alumnos = resultadoAlumnos
        )
    }

    // Factoría sencilla para instanciar el ViewModel
    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SeguimientoViewModel(repository) as T
        }
    }
}
