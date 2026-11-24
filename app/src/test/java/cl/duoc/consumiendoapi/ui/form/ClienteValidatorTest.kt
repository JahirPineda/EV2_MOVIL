package cl.duoc.consumiendoapi.ui.form

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ClienteValidatorTest {

    // ==========================================
    // Pruebas para validarNombre
    // ==========================================

    @Test
    fun validarNombre_nombreVacio_retornaError() {
        // ARRANGE
        val nombre = ""
        // ACT
        val resultado = ClienteValidator.validarNombre(nombre)
        // ASSERT
        assertEquals("Ingresa un nombre.", resultado)
    }

    @Test
    fun validarNombre_nombreEspacios_retornaError() {
        val resultado = ClienteValidator.validarNombre("   ")
        assertEquals("Ingresa un nombre.", resultado)
    }

    @Test
    fun validarNombre_nombreCorrecto_retornaNull() {
        val resultado = ClienteValidator.validarNombre("Ana")
        assertNull(resultado)
    }

    // ==========================================
    // Pruebas para validarApellido
    // ==========================================

    @Test
    fun validarApellido_apellidoVacio_retornaError() {
        val resultado = ClienteValidator.validarApellido("")
        assertEquals("Ingresa un apellido.", resultado)
    }

    @Test
    fun validarApellido_apellidoEspacios_retornaError() {
        val resultado = ClienteValidator.validarApellido("   ")
        assertEquals("Ingresa un apellido.", resultado)
    }

    @Test
    fun validarApellido_apellidoCorrecto_retornaNull() {
        val resultado = ClienteValidator.validarApellido("Soto")
        assertNull(resultado)
    }

    // ==========================================
    // Pruebas para validarEdad
    // ==========================================

    @Test
    fun validarEdad_edadVacia_retornaError() {
        val resultado = ClienteValidator.validarEdad("")
        assertEquals("Ingresa una edad.", resultado)
    }

    @Test
    fun validarEdad_edadEspacios_retornaError() {
        val resultado = ClienteValidator.validarEdad("   ")
        assertEquals("Ingresa una edad.", resultado)
    }

    @Test
    fun validarEdad_edadNoNumerica_retornaError() {
        val resultado = ClienteValidator.validarEdad("abc")
        assertEquals("La edad debe estar entre 1 y 120.", resultado)
    }

    @Test
    fun validarEdad_edadCero_retornaError() {
        val resultado = ClienteValidator.validarEdad("0")
        assertEquals("La edad debe estar entre 1 y 120.", resultado)
    }

    @Test
    fun validarEdad_limiteInferiorValido_retornaNull() {
        val resultado = ClienteValidator.validarEdad("1")
        assertNull(resultado)
    }

    @Test
    fun validarEdad_edadNormalValida_retornaNull() {
        val resultado = ClienteValidator.validarEdad("25")
        assertNull(resultado)
    }

    @Test
    fun validarEdad_limiteSuperiorValido_retornaNull() {
        val resultado = ClienteValidator.validarEdad("120")
        assertNull(resultado)
    }

    @Test
    fun validarEdad_edadFueraDeRango_retornaError() {
        val resultado = ClienteValidator.validarEdad("121")
        assertEquals("La edad debe estar entre 1 y 120.", resultado)
    }

    @Test
    fun validarEdad_edadNegativa_retornaError() {
        val resultado = ClienteValidator.validarEdad("-5")
        assertEquals("La edad debe estar entre 1 y 120.", resultado)
    }

    // ==========================================
    // Pruebas para validarTipo
    // ==========================================

    @Test
    fun validarTipo_regular_retornaNull() {
        val resultado = ClienteValidator.validarTipo("regular")
        assertNull(resultado)
    }

    @Test
    fun validarTipo_vip_retornaNull() {
        val resultado = ClienteValidator.validarTipo("vip")
        assertNull(resultado)
    }

    @Test
    fun validarTipo_vipMayuscula_retornaError() {
        val resultado = ClienteValidator.validarTipo("VIP")
        assertEquals("Selecciona un tipo válido.", resultado)
    }

    @Test
    fun validarTipo_otroTipo_retornaError() {
        val resultado = ClienteValidator.validarTipo("otro")
        assertEquals("Selecciona un tipo válido.", resultado)
    }

    @Test
    fun validarTipo_vacio_retornaError() {
        val resultado = ClienteValidator.validarTipo("")
        assertEquals("Selecciona un tipo válido.", resultado)
    }
}
