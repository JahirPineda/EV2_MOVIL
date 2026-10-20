package cl.duoc.data.repository

import cl.duoc.data.dao.BitacoraDao
import cl.duoc.data.dao.RegistroDao
import cl.duoc.data.dao.UsuarioDao
import cl.duoc.data.entity.BitacoraEntity
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio central de datos de la aplicación.
 * Actúa como puente entre la base de datos (DAOs de Room) y los ViewModels.
 */
class AppRepository(
    private val usuarioDao: UsuarioDao,
    private val bitacoraDao: BitacoraDao,
    private val registroDao: RegistroDao
) {
    // ==========================================
    // OPERACIONES DE USUARIOS
    // ==========================================

    // Busca un usuario por correo/RUT y contraseña para el login
    suspend fun login(correo: String, pass: String): UsuarioEntity? {
        return usuarioDao.login(correo, pass)
    }

    // Busca un usuario por su identificador (RUT)
    suspend fun obtenerUsuarioPorId(id: String): UsuarioEntity? {
        return usuarioDao.obtenerPorId(id)
    }

    // Obtiene la lista completa de usuarios registrados
    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>> {
        return usuarioDao.obtenerTodos()
    }

    // Registra un nuevo usuario en la base de datos
    suspend fun insertarUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun actualizarUsuario(usuario: UsuarioEntity) {
        usuarioDao.actualizarUsuario(usuario)
    }

    suspend fun eliminarUsuario(usuario: UsuarioEntity) {
        usuarioDao.eliminarUsuario(usuario)
    }

    // ==========================================
    // OPERACIONES DE BITÁCORAS
    // ==========================================

    fun obtenerBitacorasPorDocente(docenteId: String): Flow<List<BitacoraEntity>> {
        return bitacoraDao.obtenerPorDocente(docenteId)
    }

    fun obtenerBitacorasPorEstado(estado: String): Flow<List<BitacoraEntity>> {
        return bitacoraDao.obtenerPorEstado(estado)
    }

    // Obtiene todas las bitácoras guardadas en Room
    fun obtenerTodasLasBitacoras(): Flow<List<BitacoraEntity>> {
        return bitacoraDao.obtenerTodas()
    }

    // Busca una bitácora por su ID numérico
    suspend fun obtenerBitacoraPorId(id: Long): BitacoraEntity? {
        return bitacoraDao.obtenerPorId(id)
    }

    // Cambia el estado de una bitácora (ej. a "CERRADA")
    suspend fun actualizarEstadoBitacora(id: Long, estado: String) {
        bitacoraDao.actualizarEstado(id, estado)
    }

    // Guarda una nueva bitácora creada por el docente
    suspend fun insertarBitacora(bitacora: BitacoraEntity): Long {
        return bitacoraDao.insertar(bitacora)
    }

    // Actualiza los datos de una bitácora existente
    suspend fun actualizarBitacora(bitacora: BitacoraEntity) {
        bitacoraDao.actualizar(bitacora)
    }

    suspend fun eliminarBitacora(bitacora: BitacoraEntity) {
        bitacoraDao.eliminar(bitacora)
    }

    // ==========================================
    // OPERACIONES DE REGISTROS DE APRENDIZAJE
    // ==========================================

    fun obtenerRegistrosPorEstudianteYBitacora(bitacoraId: Long, estudianteId: String): Flow<List<RegistroEntity>> {
        return registroDao.obtenerPorEstudianteYBitacora(bitacoraId, estudianteId)
    }

    // Obtiene todos los aportes de estudiantes para una bitácora específica
    fun obtenerRegistrosPorBitacora(bitacoraId: Long): Flow<List<RegistroEntity>> {
        return registroDao.obtenerPorBitacora(bitacoraId)
    }

    // Obtiene el historial de registros de un estudiante
    fun obtenerRegistrosPorEstudiante(estudianteId: String): Flow<List<RegistroEntity>> {
        return registroDao.obtenerPorEstudiante(estudianteId)
    }

    // Obtiene todos los registros de todos los estudiantes
    fun obtenerTodosLosRegistros(): Flow<List<RegistroEntity>> {
        return registroDao.obtenerTodos()
    }

    // Busca un registro específico por su ID
    suspend fun obtenerRegistroPorId(id: Long): RegistroEntity? {
        return registroDao.obtenerPorId(id)
    }

    // El docente actualiza el estado (a REVISADO) y escribe su retroalimentación
    suspend fun actualizarRetroalimentacionYEstadoRegistro(id: Long, estado: String, retroalimentacion: String?) {
        registroDao.actualizarRetroalimentacionYEstado(id, estado, retroalimentacion)
    }

    // Guarda un nuevo aporte de aprendizaje del estudiante
    suspend fun insertarRegistro(registro: RegistroEntity): Long {
        return registroDao.insertar(registro)
    }

    // Actualiza un aporte de aprendizaje existente
    suspend fun actualizarRegistro(registro: RegistroEntity) {
        registroDao.actualizar(registro)
    }

    suspend fun eliminarRegistro(registro: RegistroEntity) {
        registroDao.eliminar(registro)
    }
}
