package cl.duoc.consumiendoapi.domain.model

data class Cliente(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val tipo: String
)

data class EstadisticasCliente(
    val total: Int,
    val vip: Int,
    val regulares: Int,
    val edadPromedio: Double
)
