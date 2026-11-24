package cl.duoc.data.remote

import retrofit2.Response
import retrofit2.http.*

interface BitacoraApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioResponse>

    @POST("api/usuarios")
    suspend fun registrarUsuario(@Body request: UsuarioCreateRequest): Response<UsuarioResponse>

    @GET("api/usuarios")
    suspend fun listarUsuarios(@Query("rol") rol: String? = null): Response<List<UsuarioResponse>>

    @GET("api/bitacoras")
    suspend fun listarBitacoras(
        @Query("docenteId") docenteId: String? = null,
        @Query("curso") curso: String? = null,
        @Query("estado") estado: String? = null
    ): Response<List<BitacoraResponse>>

    @POST("api/bitacoras")
    suspend fun crearBitacora(@Body request: BitacoraCreateRequest): Response<BitacoraResponse>

    @PUT("api/bitacoras/{id}")
    suspend fun editarBitacora(
        @Path("id") id: Long,
        @Body request: BitacoraCreateRequest
    ): Response<BitacoraResponse>

    @DELETE("api/bitacoras/{id}")
    suspend fun eliminarBitacora(
        @Path("id") id: Long,
        @Query("docenteId") docenteId: String? = null
    ): Response<Unit>

    @GET("api/registros")
    suspend fun listarRegistros(
        @Query("bitacoraId") bitacoraId: Long? = null,
        @Query("estudianteId") estudianteId: String? = null
    ): Response<List<RegistroResponse>>

    @POST("api/registros")
    suspend fun crearRegistro(@Body request: RegistroCreateRequest): Response<RegistroResponse>

    @PUT("api/registros/{id}")
    suspend fun editarRegistroEstudiante(
        @Path("id") id: Long,
        @Body request: RegistroUpdateRequest
    ): Response<RegistroResponse>

    @PATCH("api/registros/{id}/revision")
    suspend fun revisarRegistro(
        @Path("id") id: Long,
        @Body request: RevisionRequest
    ): Response<RegistroResponse>

    @DELETE("api/registros/{id}")
    suspend fun eliminarRegistro(
        @Path("id") id: Long,
        @Query("estudianteId") estudianteId: String? = null
    ): Response<Unit>
}