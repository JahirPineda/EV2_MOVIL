package cl.duoc.data

import cl.duoc.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Guarda en memoria la sesión del usuario que inició sesión.
 * Permite que cualquier pantalla sepa quién está conectado (nombre, rol, RUT, etc.).
 */
object SessionManager {

    // Guarda el usuario actual (o null si no hay nadie conectado)
    private val _usuarioActual = MutableStateFlow<UsuarioEntity?>(null)
    val usuarioActual: StateFlow<UsuarioEntity?> = _usuarioActual.asStateFlow()

    // Inicia la sesión guardando los datos del usuario
    fun iniciarSesion(usuario: UsuarioEntity) {
        _usuarioActual.value = usuario
    }

    // Cierra la sesión limpiando el usuario
    fun cerrarSesion() {
        _usuarioActual.value = null
    }
}
