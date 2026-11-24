package cl.duoc.consumiendoapi.ui.form

object ClienteValidator {
    fun validarNombre(nombre: String): String? {
        return if (nombre.isBlank()) "Ingresa un nombre." else null
    }

    fun validarApellido(apellido: String): String? {
        return if (apellido.isBlank()) "Ingresa un apellido." else null
    }

    fun validarEdad(edadStr: String): String? {
        val edad = edadStr.toIntOrNull()
        return when {
            edadStr.isBlank() -> "Ingresa una edad."
            edad == null || edad !in 1..120 -> "La edad debe estar entre 1 y 120."
            else -> null
        }
    }

    fun validarTipo(tipo: String): String? {
        return if (tipo !in listOf("regular", "vip")) "Selecciona un tipo válido." else null
    }
}
