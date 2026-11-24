package cl.duoc.viewmodel

import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.entity.UsuarioEntity
import cl.duoc.data.repository.AppRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SeguimientoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: AppRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarSeguimiento_conEstudiantesYRegistros_calculaMetricasCorrectas() = runTest(testDispatcher) {
        // ARRANGE: preparamos usuarios (docente y estudiantes)
        val usuarios = listOf(
            UsuarioEntity(
                id = "doc-1",
                rut = "11.111.111-1",
                nombre = "Profesor",
                correo = "profe@colegio.cl",
                contrasena = "123456",
                rol = "DOCENTE",
                cursoAsignado = "6.º básico A",
                asignaturaAsignada = "Matemáticas"
            ),
            UsuarioEntity(
                id = "est-1",
                rut = "22.222.222-2",
                nombre = "Estudiante 1",
                correo = "est1@colegio.cl",
                contrasena = "123456",
                rol = "ESTUDIANTE",
                cursoAsignado = "6.º básico A",
                asignaturaAsignada = null
            ),
            UsuarioEntity(
                id = "est-2",
                rut = "33.333.333-3",
                nombre = "Estudiante 2",
                correo = "est2@colegio.cl",
                contrasena = "123456",
                rol = "ESTUDIANTE",
                cursoAsignado = "6.º básico A",
                asignaturaAsignada = null
            )
        )

        // Registros solo para est-1 (est-2 no tiene registros)
        val registros = listOf(
            RegistroEntity(
                id = 1L,
                bitacoraId = 10L,
                estudianteId = "est-1",
                observacion = "Avance 1",
                fotoUri = null,
                fecha = "24/11/2026",
                estado = "APROBADO"
            ),
            RegistroEntity(
                id = 2L,
                bitacoraId = 10L,
                estudianteId = "est-1",
                observacion = "Avance 2",
                fotoUri = null,
                fecha = "24/11/2026",
                estado = "PENDIENTE"
            )
        )

        every { repository.obtenerTodosLosUsuarios() } returns flowOf(usuarios)
        every { repository.obtenerTodosLosRegistros() } returns flowOf(registros)

        // ACT: instanciar el ViewModel
        val viewModel = SeguimientoViewModel(repository)
        advanceUntilIdle()

        // ASSERT: comprobar métricas de seguimiento
        val state = viewModel.uiState.value
        assertEquals(2, state.totalEstudiantes)
        assertEquals(1, state.estudiantesParticipando)
        assertEquals(2, state.alumnos.size)

        val alumno1 = state.alumnos.find { it.usuario.id == "est-1" }
        val alumno2 = state.alumnos.find { it.usuario.id == "est-2" }

        assertTrue(alumno1?.haParticipado == true)
        assertEquals(2, alumno1?.cantidadRegistros)

        assertFalse(alumno2?.haParticipado == true)
        assertEquals(0, alumno2?.cantidadRegistros)
    }

    @Test
    fun factory_creaInstanciaCorrectamente() {
        val factory = SeguimientoViewModel.Factory(repository)
        val vm = factory.create(SeguimientoViewModel::class.java)
        org.junit.Assert.assertNotNull(vm)
    }
}
