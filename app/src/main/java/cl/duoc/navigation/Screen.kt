package cl.duoc.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object InicioDocente : Screen("inicio_docente")
    data object InicioEstudiante : Screen("inicio_estudiante")
    data object ListaBitacoras : Screen("lista_bitacoras/{rol}") {
        fun createRoute(rol: String) = "lista_bitacoras/$rol"
    }
    data object CrearBitacora : Screen("crear_bitacora")
    data object DetalleBitacora : Screen("detalle_bitacora/{id}/{rol}") {
        fun createRoute(id: String, rol: String) = "detalle_bitacora/$id/$rol"
    }
    data object Registro : Screen("registro/{bitacoraId}") {
        fun createRoute(bitacoraId: String) = "registro/$bitacoraId"
    }
    data object HistorialRegistros : Screen("historial_registros")
    data object RevisionDocente : Screen("revision_docente/{bitacoraId}") {
        fun createRoute(bitacoraId: String) = "revision_docente/$bitacoraId"
    }
    data object DetalleRegistroDocente : Screen("detalle_registro_docente/{registroId}") {
        fun createRoute(registroId: String) = "detalle_registro_docente/$registroId"
    }
    data object Seguimiento : Screen("seguimiento")
    data object HistorialBitacoras : Screen("historial_bitacoras")
    data object Recordatorios : Screen("recordatorios")
    data object Perfil : Screen("perfil/{rol}") {
        fun createRoute(rol: String) = "perfil/$rol"
    }
}
