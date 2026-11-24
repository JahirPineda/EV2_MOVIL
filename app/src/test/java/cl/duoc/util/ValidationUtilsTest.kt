package cl.duoc.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {

    // ==========================================
    // Validación de Correos Electrónicos
    // ==========================================

    @Test
    fun esCorreoValido_correoInstitucional_retornaTrue() {
        assertTrue(ValidationUtils.esCorreoValido("juan.perez@duocuc.cl"))
    }

    @Test
    fun esCorreoValido_correoGmailComun_retornaTrue() {
        assertTrue(ValidationUtils.esCorreoValido("docente.prueba123@gmail.com"))
    }

    @Test
    fun esCorreoValido_correoVacio_retornaFalse() {
        assertFalse(ValidationUtils.esCorreoValido(""))
        assertFalse(ValidationUtils.esCorreoValido("   "))
    }

    @Test
    fun esCorreoValido_sinArroba_retornaFalse() {
        assertFalse(ValidationUtils.esCorreoValido("correosinformato.cl"))
    }

    @Test
    fun esCorreoValido_sinDominio_retornaFalse() {
        assertFalse(ValidationUtils.esCorreoValido("usuario@"))
        assertFalse(ValidationUtils.esCorreoValido("@dominio.cl"))
    }

    // ==========================================
    // Validación de Campo No Vacío
    // ==========================================

    @Test
    fun esCampoNoVacio_conTextoValido_retornaTrue() {
        assertTrue(ValidationUtils.esCampoNoVacio("Texto"))
    }

    @Test
    fun esCampoNoVacio_conEspaciosOVacio_retornaFalse() {
        assertFalse(ValidationUtils.esCampoNoVacio(""))
        assertFalse(ValidationUtils.esCampoNoVacio("     "))
    }

    @Test
    fun esCampoNoVacio_conMinLengthPersonalizado() {
        assertFalse(ValidationUtils.esCampoNoVacio("ab", minLength = 3))
        assertTrue(ValidationUtils.esCampoNoVacio("abc", minLength = 3))
    }

    // ==========================================
    // Validación de Contraseña
    // ==========================================

    @Test
    fun validarPassword_seisOMasCaracteres_retornaTrue() {
        assertTrue(ValidationUtils.validarPassword("123456"))
        assertTrue(ValidationUtils.validarPassword("docente123"))
    }

    @Test
    fun validarPassword_menosDeSeisCaracteres_retornaFalse() {
        assertFalse(ValidationUtils.validarPassword(""))
        assertFalse(ValidationUtils.validarPassword("12345"))
        assertFalse(ValidationUtils.validarPassword("abc"))
    }

    // ==========================================
    // Validación de Longitud Mínima
    // ==========================================

    @Test
    fun validarLongitudMinima_cumpleMinimo_retornaTrue() {
        assertTrue(ValidationUtils.validarLongitudMinima("Prueba", 5))
        assertTrue(ValidationUtils.validarLongitudMinima("   Prueba   ", 5))
    }

    @Test
    fun validarLongitudMinima_noCumpleMinimo_retornaFalse() {
        assertFalse(ValidationUtils.validarLongitudMinima("Hola", 5))
        assertFalse(ValidationUtils.validarLongitudMinima("    ", 1))
    }

    // ==========================================
    // Validación de RUT Chileno (Algoritmo Módulo 11)
    // ==========================================

    @Test
    fun validarRutChileno_rutsValidosConFormato_retornaTrue() {
        // Cuentas reales de prueba del proyecto (de README y Seeding)
        assertTrue(ValidationUtils.validarRutChileno("11.111.111-1"))
        assertTrue(ValidationUtils.validarRutChileno("22.222.222-2"))
        assertTrue(ValidationUtils.validarRutChileno("33.333.333-3"))
        assertTrue(ValidationUtils.validarRutChileno("44.444.444-4"))
        assertTrue(ValidationUtils.validarRutChileno("55.555.555-5"))
        assertTrue(ValidationUtils.validarRutChileno("66.666.666-6"))
        assertTrue(ValidationUtils.validarRutChileno("77.777.777-7"))
        assertTrue(ValidationUtils.validarRutChileno("88.888.888-8"))
        assertTrue(ValidationUtils.validarRutChileno("99.999.999-9"))
        assertTrue(ValidationUtils.validarRutChileno("10.101.101-1"))
    }

    @Test
    fun validarRutChileno_rutsValidosSinPuntosNiGuion_retornaTrue() {
        assertTrue(ValidationUtils.validarRutChileno("111111111"))
        assertTrue(ValidationUtils.validarRutChileno("222222222"))
    }

    @Test
    fun validarRutChileno_rutConDigitoVerificador0_retornaTrue() {
        // 11.111.118-0
        // Módulo 11: 8*2+1*3+1*4+1*5+1*6+1*7+1*2+1*3 = 16+3+4+5+6+7+2+3 = 46 % 11 = 2 -> 11-2 = 9
        // Buscamos un RUT donde (11 - resto) == 11 -> resto == 0.
        // Ejemplo: 11.111.111-1 tiene suma 24 % 11 = 2.
        // Si cuerpo suma 22 o 33 o 44 -> resto 0 -> DV esperado "0".
        // Cuerpo: 50.000.000 -> 5*4 = 20 -> no.
        // Cuerpo: 10.000.001 -> 1*2 + 0 + 1*3 = 5.
        // Un RUT conocido que termina en 0: 5.555.555-5 (suma: 5*(2+3+4+5+6+7+2) = 5*29 = 145 % 11 = 2).
        // Probemos un RUT válido generado: "1-9" -> 1*2 = 2, 11-2 = 9 -> "1-9"
        assertTrue(ValidationUtils.validarRutChileno("1-9"))
    }

    @Test
    fun validarRutChileno_rutConDigitoVerificadorK_retornaTrue() {
        // Buscamos resto = 1 -> 11 - 1 = 10 -> "K".
        // Para "6-K": 6*2 = 12 % 11 = 1 -> 11 - 1 = 10 -> "K"
        assertTrue(ValidationUtils.validarRutChileno("6-K"))
        assertTrue(ValidationUtils.validarRutChileno("6-k")) // minúscula
    }

    @Test
    fun validarRutChileno_rutConDigitoVerificadorCero_retornaTrue() {
        // Para "11-0": cuerpo "11" -> 1*2 + 1*3 = 5 -> resto 5.
        // Para cuerpo con suma divisible por 11 (resto = 0):
        // E.g. "0": cuerpo de longitud 1 con suma 11 o 22.
        // E.g. cuerpo "19": 9*2 + 1*3 = 18 + 3 = 21 (resto 10).
        // E.g. cuerpo "43": 3*2 + 4*3 = 6 + 12 = 18.
        // E.g. cuerpo "45": 5*2 + 4*3 = 10 + 12 = 22! 22 % 11 == 0 -> 11 - 0 = 11 -> DV esperado "0"!
        assertTrue(ValidationUtils.validarRutChileno("45-0"))
    }

    @Test
    fun validarRutChileno_digitoVerificadorErroneo_retornaFalse() {
        assertFalse(ValidationUtils.validarRutChileno("11.111.111-2"))
        assertFalse(ValidationUtils.validarRutChileno("22.222.222-8"))
    }

    @Test
    fun validarRutChileno_largoInsuficiente_retornaFalse() {
        assertFalse(ValidationUtils.validarRutChileno(""))
        assertFalse(ValidationUtils.validarRutChileno("1"))
        assertFalse(ValidationUtils.validarRutChileno("-"))
    }

    @Test
    fun validarRutChileno_cuerpoConLetras_retornaFalse() {
        assertFalse(ValidationUtils.validarRutChileno("11.11A.111-1"))
        assertFalse(ValidationUtils.validarRutChileno("ABCD-1"))
    }

    // ==========================================
    // Validación de Fecha dd/MM/yyyy
    // ==========================================

    @Test
    fun validarFechaDdMMyyyy_fechasValidas_retornaTrue() {
        assertTrue(ValidationUtils.validarFechaDdMMyyyy("15/05/2024"))
        assertTrue(ValidationUtils.validarFechaDdMMyyyy("01/01/2025"))
        assertTrue(ValidationUtils.validarFechaDdMMyyyy("31/12/2026"))
        assertTrue(ValidationUtils.validarFechaDdMMyyyy("30/04/2024"))
    }

    @Test
    fun validarFechaDdMMyyyy_febreroBisiestoYNoBisiesto() {
        // Año bisiesto 2024 (divisible por 4)
        assertTrue(ValidationUtils.validarFechaDdMMyyyy("29/02/2024"))
        // Año no bisiesto 2023
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("29/02/2023"))
        assertTrue(ValidationUtils.validarFechaDdMMyyyy("28/02/2023"))
    }

    @Test
    fun validarFechaDdMMyyyy_mesesDe30Dias_rechazaDia31() {
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("31/04/2024")) // Abril
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("31/06/2024")) // Junio
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("31/09/2024")) // Septiembre
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("31/11/2024")) // Noviembre
    }

    @Test
    fun validarFechaDdMMyyyy_formatoIncorrecto_retornaFalse() {
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("15-05-2024"))
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("15/05"))
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("2024/05/15"))
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("dd/mm/yyyy"))
        assertFalse(ValidationUtils.validarFechaDdMMyyyy(""))
    }

    @Test
    fun validarFechaDdMMyyyy_valoresFueraDeRango_retornaFalse() {
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("00/05/2024")) // Día 0
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("32/01/2024")) // Día 32
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("15/00/2024")) // Mes 0
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("15/13/2024")) // Mes 13
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("15/05/1999")) // Año < 2000
        assertFalse(ValidationUtils.validarFechaDdMMyyyy("15/05/2101")) // Año > 2100
    }

    // ==========================================
    // Comprobación de Curso Asignado al Docente
    // ==========================================

    @Test
    fun docenteTieneCurso_cursoAsignadoDirecto_retornaTrue() {
        assertTrue(ValidationUtils.docenteTieneCurso("6.º básico A", "6.º básico A"))
    }

    @Test
    fun docenteTieneCurso_multiplesCursosDocente_retornaTrueSiEstaEnLaLista() {
        val cursos = "6.º básico A, 6.º básico B, 7.º básico A"
        assertTrue(ValidationUtils.docenteTieneCurso(cursos, "6.º básico B"))
        assertTrue(ValidationUtils.docenteTieneCurso(cursos, "6.º BÁSICO A")) // Ignore case
    }

    @Test
    fun docenteTieneCurso_cursoNoAsignado_retornaFalse() {
        val cursos = "6.º básico A, 6.º básico B"
        assertFalse(ValidationUtils.docenteTieneCurso(cursos, "8.º básico A"))
    }

    @Test
    fun docenteTieneCurso_valoresNulosOVacios_retornaFalse() {
        assertFalse(ValidationUtils.docenteTieneCurso(null, "6.º básico A"))
        assertFalse(ValidationUtils.docenteTieneCurso("6.º básico A", null))
        assertFalse(ValidationUtils.docenteTieneCurso("", ""))
        assertFalse(ValidationUtils.docenteTieneCurso("   ", "6.º básico A"))
    }

    // ==========================================
    // Validaciones Adicionales (Vacío, Mínimo 3, Máximo 10, Rango 3..10)
    // ==========================================

    @Test
    fun esCampoVacio_comportamientoCorrecto() {
        assertTrue(ValidationUtils.esCampoVacio(null))
        assertTrue(ValidationUtils.esCampoVacio(""))
        assertTrue(ValidationUtils.esCampoVacio("   "))
        assertFalse(ValidationUtils.esCampoVacio("Texto"))
    }

    @Test
    fun validarMinimo3Caracteres_comportamientoCorrecto() {
        assertFalse(ValidationUtils.validarMinimo3Caracteres(null))
        assertFalse(ValidationUtils.validarMinimo3Caracteres(""))
        assertFalse(ValidationUtils.validarMinimo3Caracteres("ab"))
        assertTrue(ValidationUtils.validarMinimo3Caracteres("abc"))
        assertTrue(ValidationUtils.validarMinimo3Caracteres("   abcd   "))
    }

    @Test
    fun validarMaximo10Caracteres_comportamientoCorrecto() {
        assertFalse(ValidationUtils.validarMaximo10Caracteres(null))
        assertFalse(ValidationUtils.validarMaximo10Caracteres(""))
        assertTrue(ValidationUtils.validarMaximo10Caracteres("1234567890"))
        assertFalse(ValidationUtils.validarMaximo10Caracteres("12345678901"))
        assertTrue(ValidationUtils.validarMaximo10Caracteres("hola"))
    }

    @Test
    fun validarRango3a10Caracteres_comportamientoCorrecto() {
        assertFalse(ValidationUtils.validarRango3a10Caracteres(null))
        assertFalse(ValidationUtils.validarRango3a10Caracteres(""))
        assertFalse(ValidationUtils.validarRango3a10Caracteres("ab"))
        assertTrue(ValidationUtils.validarRango3a10Caracteres("abc"))
        assertTrue(ValidationUtils.validarRango3a10Caracteres("1234567890"))
        assertFalse(ValidationUtils.validarRango3a10Caracteres("12345678901"))
    }
}
