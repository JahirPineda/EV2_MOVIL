package cl.duoc.navigation

/**
 * Define las rutas de navegación de todas las pantallas de la aplicación.
 * El uso de una clase sellada (sealed class) ayuda a evitar errores de tipeo al navegar.
 */
sealed class Screen(val route: String) {

    // 1. Pantalla de inicio de sesión
    data object Login : Screen("login")

    // 2. Registro de un nuevo usuario
    data object Register : Screen("register")

    // 3. Paneles principales según el rol
    data object DocenteDashboard : Screen("docente_dashboard")
    data object EstudianteDashboard : Screen("estudiante_dashboard")

    // 4. Lista de bitácoras (recibe el rol del usuario como parámetro)
    data object ListaBitacoras : Screen("lista_bitacoras/{rol}") {
        fun createRoute(rol: String) = "lista_bitacoras/$rol"
    }

    // 5. Crear o editar bitácora (parámetro opcional: bitacoraId)
    data object CrearBitacora : Screen("crear_bitacora?bitacoraId={bitacoraId}") {
        fun createRoute(bitacoraId: Long? = null): String {
            return if (bitacoraId != null) "crear_bitacora?bitacoraId=$bitacoraId" else "crear_bitacora"
        }
    }

    // 6. Detalle de una bitácora (recibe id y rol)
    data object DetalleBitacora : Screen("detalle_bitacora/{id}/{rol}") {
        fun createRoute(id: String, rol: String) = "detalle_bitacora/$id/$rol"
    }

    // 7. Registro o edición de aporte de aprendizaje del estudiante
    data object Registro : Screen("registro/{bitacoraId}?registroId={registroId}") {
        fun createRoute(bitacoraId: Long, registroId: Long? = null): String {
            return if (registroId != null) "registro/$bitacoraId?registroId=$registroId" else "registro/$bitacoraId"
        }
    }

    // 8. Historial de aportes del estudiante
    data object HistorialRegistros : Screen("historial_registros")

    // 9. Revisión de aportes para el docente
    data object RevisionDocente : Screen("revision_docente/{bitacoraId}") {
        fun createRoute(bitacoraId: String) = "revision_docente/$bitacoraId"
    }

    // 10. Detalle de un aporte para que el docente lo retroalimente
    data object DetalleRegistroDocente : Screen("detalle_registro_docente/{registroId}") {
        fun createRoute(registroId: String) = "detalle_registro_docente/$registroId"
    }

    // 11. Pantallas adicionales del docente
    data object Seguimiento : Screen("seguimiento")
    data object HistorialBitacoras : Screen("historial_bitacoras")
    data object Recordatorios : Screen("recordatorios")

    // 12. Pantalla de perfil (común para ambos roles)
    data object Perfil : Screen("perfil/{rol}") {
        fun createRoute(rol: String) = "perfil/$rol"
    }
}
