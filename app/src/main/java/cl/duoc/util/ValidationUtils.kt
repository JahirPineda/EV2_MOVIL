package cl.duoc.util

import android.util.Patterns

/**
 * Utilidades sencillas para validar los formularios de la aplicación.
 * Diseñado de forma clara para que sea fácil de entender y explicar.
 */
object ValidationUtils {

    // Comprueba si el correo tiene un formato válido (ejemplo: usuario@correo.cl)
    fun esCorreoValido(correo: String): Boolean {
        return correo.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }

    // Comprueba que un texto no esté vacío y cumpla un largo mínimo
    fun esCampoNoVacio(valor: String, minLength: Int = 1): Boolean {
        return valor.trim().length >= minLength
    }

    // Valida que la contraseña tenga al menos 6 caracteres
    fun validarPassword(password: String): Boolean {
        return password.length >= 6
    }

    // Valida que el texto cumpla con una cantidad mínima de caracteres
    fun validarLongitudMinima(texto: String, minLength: Int): Boolean {
        return texto.trim().length >= minLength
    }

    /**
     * Valida el formato y dígito verificador del RUT chileno usando el algoritmo Módulo 11.
     * Funciona tanto con puntos y guión (ej: 11.111.111-1) como sin ellos (ej: 111111111).
     */
    fun validarRutChileno(rut: String): Boolean {
        // 1. Limpiamos puntos, guiones y espacios, pasando a mayúsculas
        val rutLimpio = rut.replace(".", "").replace("-", "").trim().uppercase()
        
        // Un RUT válido debe tener al menos 2 caracteres (número + dígito verificador)
        if (rutLimpio.length < 2) return false

        // 2. Separamos el número (cuerpo) del dígito verificador (último caracter)
        val cuerpo = rutLimpio.dropLast(1)
        val dv = rutLimpio.takeLast(1)

        // Verificamos que el cuerpo contenga solo dígitos
        if (!cuerpo.all { it.isDigit() }) return false

        // 3. Aplicamos el algoritmo de Módulo 11:
        // Multiplicamos cada dígito de derecha a izquierda por la serie 2, 3, 4, 5, 6, 7...
        var suma = 0
        var multiplicador = 2
        for (i in cuerpo.reversed()) {
            val digito = Character.getNumericValue(i)
            suma += digito * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        // 4. Calculamos el dígito verificador esperado
        val resto = suma % 11
        val dvEsperado = when (11 - resto) {
            11 -> "0"
            10 -> "K"
            else -> (11 - resto).toString()
        }

        // 5. Comparamos el dígito ingresado con el esperado
        return dv == dvEsperado
    }

    /**
     * Valida que la fecha tenga el formato dd/MM/yyyy y que sea una fecha real.
     */
    fun validarFechaDdMMyyyy(fecha: String): Boolean {
        // Verificamos que tenga dos números, barra, dos números, barra y cuatro números
        val partes = fecha.split("/")
        if (partes.size != 3) return false

        val dia = partes[0].toIntOrNull() ?: return false
        val mes = partes[1].toIntOrNull() ?: return false
        val anio = partes[2].toIntOrNull() ?: return false

        // Validamos rangos razonables de año y mes
        if (anio < 2000 || anio > 2100) return false
        if (mes < 1 || mes > 12) return false

        // Determinamos la cantidad de días máximos del mes
        val maxDias = when (mes) {
            2 -> {
                // Febrero bisiesto si el año es divisible por 4
                if (anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)) 29 else 28
            }
            4, 6, 9, 11 -> 30
            else -> 31
        }

        return dia in 1..maxDias
    }
}
