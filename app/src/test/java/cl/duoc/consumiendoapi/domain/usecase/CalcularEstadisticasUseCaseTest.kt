package cl.duoc.consumiendoapi.domain.usecase

import cl.duoc.consumiendoapi.domain.model.Cliente
import org.junit.Assert.assertEquals
import org.junit.Test

class CalcularEstadisticasUseCaseTest {

    @Test
    fun calcularEstadisticas_conTresClientes_retornaTotalesCorrectos() {
        // ARRANGE: preparar entradas y objeto a probar
        val clientes = listOf(
            Cliente(1, "Ana", "Pérez", 20, "vip"),
            Cliente(2, "Pedro", "Rojas", 30, "regular"),
            Cliente(3, "María", "Soto", 40, "vip")
        )
        val useCase = CalcularEstadisticasUseCase()

        // ACT: ejecutar la operación
        val resultado = useCase.calcularEstadisticas(clientes)

        // ASSERT: comparar esperado y real
        assertEquals(3, resultado.total)
        assertEquals(2, resultado.vip)
        assertEquals(1, resultado.regulares)
        assertEquals(30.0, resultado.edadPromedio, 0.001)
    }

    @Test
    fun calcularEstadisticas_listaVacia_retornaCeros() {
        val useCase = CalcularEstadisticasUseCase()
        val resultado = useCase.calcularEstadisticas(emptyList())

        assertEquals(0, resultado.total)
        assertEquals(0, resultado.vip)
        assertEquals(0, resultado.regulares)
        assertEquals(0.0, resultado.edadPromedio, 0.001)
    }

    @Test
    fun calcularEstadisticas_todosVip_retornaSoloVip() {
        val clientes = listOf(
            Cliente(1, "Ana", "Pérez", 25, "vip"),
            Cliente(2, "Luis", "Gómez", 35, "vip")
        )
        val useCase = CalcularEstadisticasUseCase()
        val resultado = useCase.calcularEstadisticas(clientes)

        assertEquals(2, resultado.total)
        assertEquals(2, resultado.vip)
        assertEquals(0, resultado.regulares)
        assertEquals(30.0, resultado.edadPromedio, 0.001)
    }

    @Test
    fun calcularEstadisticas_todosRegulares_retornaSoloRegulares() {
        val clientes = listOf(
            Cliente(1, "Pedro", "Rojas", 20, "regular"),
            Cliente(2, "Sofía", "Castro", 40, "regular")
        )
        val useCase = CalcularEstadisticasUseCase()
        val resultado = useCase.calcularEstadisticas(clientes)

        assertEquals(2, resultado.total)
        assertEquals(0, resultado.vip)
        assertEquals(2, resultado.regulares)
        assertEquals(30.0, resultado.edadPromedio, 0.001)
    }

    @Test
    fun calcularEstadisticas_usandoOperadorInvoke_retornaTotalesCorrectos() {
        val clientes = listOf(
            Cliente(1, "Ana", "Pérez", 20, "vip"),
            Cliente(2, "Pedro", "Rojas", 30, "regular"),
            Cliente(3, "María", "Soto", 40, "vip")
        )
        val useCase = CalcularEstadisticasUseCase()
        val resultado = useCase(clientes)

        assertEquals(3, resultado.total)
        assertEquals(2, resultado.vip)
        assertEquals(1, resultado.regulares)
        assertEquals(30.0, resultado.edadPromedio, 0.001)
    }
}
