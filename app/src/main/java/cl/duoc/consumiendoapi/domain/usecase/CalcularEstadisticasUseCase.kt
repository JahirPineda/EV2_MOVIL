package cl.duoc.consumiendoapi.domain.usecase

import cl.duoc.consumiendoapi.domain.model.Cliente
import cl.duoc.consumiendoapi.domain.model.EstadisticasCliente

class CalcularEstadisticasUseCase {

    fun calcularEstadisticas(clientes: List<Cliente>): EstadisticasCliente {
        if (clientes.isEmpty()) {
            return EstadisticasCliente(
                total = 0,
                vip = 0,
                regulares = 0,
                edadPromedio = 0.0
            )
        }

        val total = clientes.size
        val vip = clientes.count { it.tipo.equals("vip", ignoreCase = true) }
        val regulares = clientes.count { it.tipo.equals("regular", ignoreCase = true) }
        val edadPromedio = clientes.map { it.edad }.average()

        return EstadisticasCliente(
            total = total,
            vip = vip,
            regulares = regulares,
            edadPromedio = edadPromedio
        )
    }

    operator fun invoke(clientes: List<Cliente>): EstadisticasCliente = calcularEstadisticas(clientes)
}
