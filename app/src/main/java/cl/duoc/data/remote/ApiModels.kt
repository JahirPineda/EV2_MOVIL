package cl.duoc.data.remote

data class LoginRequest(
    val rut: String,
    val contrasena: String
)

data class UsuarioCreateRequest(
    val rut: String,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: String,
    val cursoAsignado: String? = null,
    val asignaturaAsignada: String? = null
)

data class UsuarioResponse(
    val id: String,
    val rut: String,
    val nombre: String,
    val correo: String,
    val rol: String,
    val cursoAsignado: String?,
    val asignaturaAsignada: String?,
    val token: String? = null
)

data class BitacoraCreateRequest(
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val curso: String,
    val estado: String = "PUBLICADA",
    val docenteId: String
)

data class BitacoraResponse(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val curso: String,
    val estado: String,
    val docenteId: String,
    val docenteNombre: String? = null
)

data class RegistroCreateRequest(
    val bitacoraId: Long,
    val estudianteId: String,
    val observacion: String,
    val fotoUri: String? = null,
    val fecha: String
)

data class RegistroUpdateRequest(
    val estudianteId: String,
    val observacion: String,
    val fotoUri: String? = null
)

data class RevisionRequest(
    val docenteId: String,
    val estado: String = "REVISADO",
    val retroalimentacionDocente: String
)

data class RegistroResponse(
    val id: Long,
    val bitacoraId: Long,
    val estudianteId: String,
    val estudianteNombre: String? = null,
    val observacion: String,
    val fotoUri: String?,
    val fecha: String,
    val estado: String,
    val retroalimentacionDocente: String?
)