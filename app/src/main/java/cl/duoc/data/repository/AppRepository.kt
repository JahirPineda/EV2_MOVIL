package cl.duoc.data.repository

import android.util.Log
import cl.duoc.data.dao.BitacoraDao
import cl.duoc.data.dao.RegistroDao
import cl.duoc.data.dao.UsuarioDao
import cl.duoc.data.entity.BitacoraEntity
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.entity.UsuarioEntity
import cl.duoc.data.remote.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONObject

/**
 * Repositorio central de datos de la aplicación.
 * Integra la API REST de Spring Boot (Retrofit) con persistencia local (Room).
 */
class AppRepository(
    private val usuarioDao: UsuarioDao,
    private val bitacoraDao: BitacoraDao,
    private val registroDao: RegistroDao,
    private val apiService: BitacoraApiService = RetrofitClient.apiService
) {
    companion object {
        private const val TAG = "AppRepository"
    }

    private fun extraerMensajeError(errorBody: String?): String {
        return try {
            val json = JSONObject(errorBody ?: "")
            json.optString("message", "Error en la solicitud al servidor")
        } catch (e: Exception) {
            "Error de comunicación con el servidor"
        }
    }

    // ==========================================
    // SINCRONIZACIÓN REMOTA DESDE SPRING BOOT
    // ==========================================

    /**
     * Descarga usuarios, bitácoras y registros reales desde MySQL (Spring Boot)
     * y actualiza la base local Room para reflejar los cambios en la UI.
     */
    suspend fun sincronizarDesdeServidor() {
        try {
            // 1. Sincronizar Usuarios primero (por las Foreign Keys de Room)
            val respUsuarios = apiService.listarUsuarios()
            if (respUsuarios.isSuccessful) {
                respUsuarios.body()?.forEach { u ->
                    usuarioDao.insertarUsuario(
                        UsuarioEntity(
                            id = u.id,
                            rut = u.rut,
                            nombre = u.nombre,
                            correo = u.correo,
                            contrasena = "",
                            rol = u.rol,
                            cursoAsignado = u.cursoAsignado,
                            asignaturaAsignada = u.asignaturaAsignada
                        )
                    )
                }
            }

            // 2. Sincronizar Bitácoras
            val respBitacoras = apiService.listarBitacoras()
            if (respBitacoras.isSuccessful) {
                respBitacoras.body()?.forEach { b ->
                    bitacoraDao.insertar(
                        BitacoraEntity(
                            id = b.id,
                            titulo = b.titulo,
                            descripcion = b.descripcion,
                            fecha = b.fecha,
                            curso = b.curso,
                            estado = b.estado,
                            docenteId = b.docenteId
                        )
                    )
                }
            }

            // 3. Sincronizar Registros de Aprendizaje
            val respRegistros = apiService.listarRegistros()
            if (respRegistros.isSuccessful) {
                respRegistros.body()?.forEach { r ->
                    registroDao.insertar(
                        RegistroEntity(
                            id = r.id,
                            bitacoraId = r.bitacoraId,
                            estudianteId = r.estudianteId,
                            observacion = r.observacion,
                            fotoUri = r.fotoUri,
                            fecha = r.fecha,
                            estado = r.estado,
                            retroalimentacionDocente = r.retroalimentacionDocente
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sincronizando con Spring Boot: ${e.message}")
        }
    }

    // ==========================================
    // OPERACIONES DE USUARIOS (AUTH + CRUD)
    // ==========================================

    suspend fun login(correo: String, pass: String): UsuarioEntity? {
        try {
            val response = apiService.login(LoginRequest(rut = correo.trim(), contrasena = pass))
            if (response.isSuccessful && response.body() != null) {
                val u = response.body()!!
                val entity = UsuarioEntity(
                    id = u.id,
                    rut = u.rut,
                    nombre = u.nombre,
                    correo = u.correo,
                    contrasena = pass,
                    rol = u.rol,
                    cursoAsignado = u.cursoAsignado,
                    asignaturaAsignada = u.asignaturaAsignada
                )
                usuarioDao.insertarUsuario(entity)
                // Al iniciar sesión, descargamos los datos actualizados desde MySQL
                sincronizarDesdeServidor()
                return entity
            } else {
                val msg = extraerMensajeError(response.errorBody()?.string())
                Log.w(TAG, "Login rechazado por backend: $msg")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fallo de red en login, usando respaldo local Room: ${e.message}")
            return usuarioDao.login(correo, pass)
        }
    }

    suspend fun obtenerUsuarioPorId(id: String): UsuarioEntity? {
        return usuarioDao.obtenerPorId(id)
    }

    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>> {
        return usuarioDao.obtenerTodos()
    }

    suspend fun insertarUsuario(usuario: UsuarioEntity) {
        val response = apiService.registrarUsuario(
            UsuarioCreateRequest(
                rut = usuario.rut,
                nombre = usuario.nombre,
                correo = usuario.correo,
                contrasena = usuario.contrasena,
                rol = usuario.rol,
                cursoAsignado = usuario.cursoAsignado,
                asignaturaAsignada = usuario.asignaturaAsignada
            )
        )
        if (response.isSuccessful && response.body() != null) {
            val creado = response.body()!!
            usuarioDao.insertarUsuario(
                usuario.copy(id = creado.id, rut = creado.rut)
            )
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    suspend fun actualizarUsuario(usuario: UsuarioEntity) {
        usuarioDao.actualizarUsuario(usuario)
    }

    suspend fun eliminarUsuario(usuario: UsuarioEntity) {
        usuarioDao.eliminarUsuario(usuario)
    }

    // ==========================================
    // OPERACIONES DE BITÁCORAS (CRUD REMOTO + ROOM)
    // ==========================================

    fun obtenerBitacorasPorDocente(docenteId: String): Flow<List<BitacoraEntity>> {
        return bitacoraDao.obtenerPorDocente(docenteId)
    }

    fun obtenerBitacorasPorEstado(estado: String): Flow<List<BitacoraEntity>> {
        return bitacoraDao.obtenerPorEstado(estado)
    }

    fun obtenerTodasLasBitacoras(): Flow<List<BitacoraEntity>> {
        return bitacoraDao.obtenerTodas()
    }

    suspend fun obtenerBitacoraPorId(id: Long): BitacoraEntity? {
        return bitacoraDao.obtenerPorId(id)
    }

    suspend fun actualizarEstadoBitacora(id: Long, estado: String) {
        val actual = bitacoraDao.obtenerPorId(id)
        if (actual != null) {
            actualizarBitacora(actual.copy(estado = estado))
        } else {
            bitacoraDao.actualizarEstado(id, estado)
        }
    }

    suspend fun insertarBitacora(bitacora: BitacoraEntity): Long {
        val response = apiService.crearBitacora(
            BitacoraCreateRequest(
                titulo = bitacora.titulo,
                descripcion = bitacora.descripcion,
                fecha = bitacora.fecha,
                curso = bitacora.curso,
                estado = bitacora.estado,
                docenteId = bitacora.docenteId
            )
        )
        if (response.isSuccessful && response.body() != null) {
            val remota = response.body()!!
            val guardada = bitacora.copy(id = remota.id)
            bitacoraDao.insertar(guardada)
            return remota.id
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    suspend fun actualizarBitacora(bitacora: BitacoraEntity) {
        val response = apiService.editarBitacora(
            id = bitacora.id,
            request = BitacoraCreateRequest(
                titulo = bitacora.titulo,
                descripcion = bitacora.descripcion,
                fecha = bitacora.fecha,
                curso = bitacora.curso,
                estado = bitacora.estado,
                docenteId = bitacora.docenteId
            )
        )
        if (response.isSuccessful) {
            bitacoraDao.actualizar(bitacora)
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    suspend fun eliminarBitacora(bitacora: BitacoraEntity) {
        val response = apiService.eliminarBitacora(
            id = bitacora.id,
            docenteId = bitacora.docenteId
        )
        if (response.isSuccessful) {
            bitacoraDao.eliminar(bitacora)
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    // ==========================================
    // OPERACIONES DE REGISTROS DE APRENDIZAJE
    // ==========================================

    fun obtenerRegistrosPorEstudianteYBitacora(bitacoraId: Long, estudianteId: String): Flow<List<RegistroEntity>> {
        return registroDao.obtenerPorEstudianteYBitacora(bitacoraId, estudianteId)
    }

    fun obtenerRegistrosPorBitacora(bitacoraId: Long): Flow<List<RegistroEntity>> {
        return registroDao.obtenerPorBitacora(bitacoraId)
    }

    fun obtenerRegistrosPorEstudiante(estudianteId: String): Flow<List<RegistroEntity>> {
        return registroDao.obtenerPorEstudiante(estudianteId)
    }

    fun obtenerTodosLosRegistros(): Flow<List<RegistroEntity>> {
        return registroDao.obtenerTodos()
    }

    suspend fun obtenerRegistroPorId(id: Long): RegistroEntity? {
        return registroDao.obtenerPorId(id)
    }

    suspend fun actualizarRetroalimentacionYEstadoRegistro(id: Long, estado: String, retroalimentacion: String?) {
        val registroActual = registroDao.obtenerPorId(id)
        val bitacora = registroActual?.let { bitacoraDao.obtenerPorId(it.bitacoraId) }
        val docenteId = bitacora?.docenteId ?: "11111111-1"

        val response = apiService.revisarRegistro(
            id = id,
            request = RevisionRequest(
                docenteId = docenteId,
                estado = estado,
                retroalimentacionDocente = retroalimentacion ?: ""
            )
        )
        if (response.isSuccessful) {
            registroDao.actualizarRetroalimentacionYEstado(id, estado, retroalimentacion)
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    suspend fun insertarRegistro(registro: RegistroEntity): Long {
        val response = apiService.crearRegistro(
            RegistroCreateRequest(
                bitacoraId = registro.bitacoraId,
                estudianteId = registro.estudianteId,
                observacion = registro.observacion,
                fotoUri = registro.fotoUri,
                fecha = registro.fecha
            )
        )
        if (response.isSuccessful && response.body() != null) {
            val remoto = response.body()!!
            val guardado = registro.copy(id = remoto.id, estado = remoto.estado)
            registroDao.insertar(guardado)
            return remoto.id
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    suspend fun actualizarRegistro(registro: RegistroEntity) {
        val response = apiService.editarRegistroEstudiante(
            id = registro.id,
            request = RegistroUpdateRequest(
                estudianteId = registro.estudianteId,
                observacion = registro.observacion,
                fotoUri = registro.fotoUri
            )
        )
        if (response.isSuccessful) {
            registroDao.actualizar(registro)
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }

    suspend fun eliminarRegistro(registro: RegistroEntity) {
        val response = apiService.eliminarRegistro(
            id = registro.id,
            estudianteId = registro.estudianteId
        )
        if (response.isSuccessful) {
            registroDao.eliminar(registro)
        } else {
            throw Exception(extraerMensajeError(response.errorBody()?.string()))
        }
    }
}