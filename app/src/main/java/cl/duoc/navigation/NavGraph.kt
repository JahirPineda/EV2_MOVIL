package cl.duoc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cl.duoc.screens.CrearBitacoraScreen
import cl.duoc.screens.DetalleBitacoraScreen
import cl.duoc.screens.DetalleRegistroDocenteScreen
import cl.duoc.screens.HistorialBitacorasScreen
import cl.duoc.screens.HistorialRegistrosScreen
import cl.duoc.screens.InicioDocenteScreen
import cl.duoc.screens.InicioEstudianteScreen
import cl.duoc.screens.ListaBitacorasScreen
import cl.duoc.screens.LoginScreen
import cl.duoc.screens.PerfilScreen
import cl.duoc.screens.RecordatoriosScreen
import cl.duoc.screens.RegistroScreen
import cl.duoc.screens.RevisionDocenteScreen
import cl.duoc.screens.SeguimientoScreen

@Composable
fun BitacoraNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // 1. Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginDocente = {
                    navController.navigate(Screen.InicioDocente.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginEstudiante = {
                    navController.navigate(Screen.InicioEstudiante.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Inicio Docente
        composable(Screen.InicioDocente.route) {
            InicioDocenteScreen(
                onCrearBitacora = { navController.navigate(Screen.CrearBitacora.route) },
                onVerBitacoras = { navController.navigate(Screen.ListaBitacoras.createRoute("docente")) },
                onVerSeguimiento = { navController.navigate(Screen.Seguimiento.route) },
                onVerHistorial = { navController.navigate(Screen.HistorialBitacoras.route) },
                onVerRecordatorios = { navController.navigate(Screen.Recordatorios.route) },
                onVerPerfil = { navController.navigate(Screen.Perfil.createRoute("docente")) },
                onVerDetalleBitacora = { id ->
                    navController.navigate(Screen.DetalleBitacora.createRoute(id, "docente"))
                }
            )
        }

        // 3. Inicio Estudiante
        composable(Screen.InicioEstudiante.route) {
            InicioEstudianteScreen(
                onRegistrarAprendizaje = { bitacoraId ->
                    navController.navigate(Screen.Registro.createRoute(bitacoraId))
                },
                onVerBitacoras = { navController.navigate(Screen.ListaBitacoras.createRoute("estudiante")) },
                onVerHistorial = { navController.navigate(Screen.HistorialRegistros.route) },
                onVerPerfil = { navController.navigate(Screen.Perfil.createRoute("estudiante")) },
                onVerDetalleBitacora = { id ->
                    navController.navigate(Screen.DetalleBitacora.createRoute(id, "estudiante"))
                }
            )
        }

        // 4. Lista Bitácoras
        composable(
            route = Screen.ListaBitacoras.route,
            arguments = listOf(navArgument("rol") { type = NavType.StringType })
        ) { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rol") ?: "estudiante"
            ListaBitacorasScreen(
                rol = rol,
                onNavigateBack = { navController.popBackStack() },
                onVerDetalleBitacora = { id ->
                    navController.navigate(Screen.DetalleBitacora.createRoute(id, rol))
                }
            )
        }

        // 5. Crear Bitácora (Docente)
        composable(Screen.CrearBitacora.route) {
            CrearBitacoraScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. Detalle Bitácora (Común)
        composable(
            route = Screen.DetalleBitacora.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: "1"
            val rol = backStackEntry.arguments?.getString("rol") ?: "estudiante"
            DetalleBitacoraScreen(
                bitacoraId = id,
                rol = rol,
                onNavigateBack = { navController.popBackStack() },
                onRegistrarAprendizaje = { bId ->
                    navController.navigate(Screen.Registro.createRoute(bId))
                },
                onRevisarAportes = { bId ->
                    navController.navigate(Screen.RevisionDocente.createRoute(bId))
                },
                onVerMisRegistros = {
                    navController.navigate(Screen.HistorialRegistros.route)
                }
            )
        }

        // 7. Registro de Aprendizaje (Estudiante)
        composable(
            route = Screen.Registro.route,
            arguments = listOf(navArgument("bitacoraId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bitacoraId = backStackEntry.arguments?.getString("bitacoraId") ?: "1"
            RegistroScreen(
                bitacoraId = bitacoraId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 8. Historial de Registros (Estudiante)
        composable(Screen.HistorialRegistros.route) {
            HistorialRegistrosScreen(
                onNavigateBack = { navController.popBackStack() },
                onVerDetalleRegistro = { regId ->
                    navController.navigate(Screen.DetalleRegistroDocente.createRoute(regId))
                }
            )
        }

        // 9. Revisión Docente
        composable(
            route = Screen.RevisionDocente.route,
            arguments = listOf(navArgument("bitacoraId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bitacoraId = backStackEntry.arguments?.getString("bitacoraId") ?: "1"
            RevisionDocenteScreen(
                bitacoraId = bitacoraId,
                onNavigateBack = { navController.popBackStack() },
                onVerDetalleRegistro = { regId ->
                    navController.navigate(Screen.DetalleRegistroDocente.createRoute(regId))
                }
            )
        }

        // 10. Detalle Registro Docente (Feedback)
        composable(
            route = Screen.DetalleRegistroDocente.route,
            arguments = listOf(navArgument("registroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val registroId = backStackEntry.arguments?.getString("registroId") ?: "1"
            DetalleRegistroDocenteScreen(
                registroId = registroId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 11. Seguimiento (Docente)
        composable(Screen.Seguimiento.route) {
            SeguimientoScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 12. Historial de Bitácoras Pasadas (Docente)
        composable(Screen.HistorialBitacoras.route) {
            HistorialBitacorasScreen(
                onNavigateBack = { navController.popBackStack() },
                onVerBitacora = { id ->
                    navController.navigate(Screen.DetalleBitacora.createRoute(id, "docente"))
                },
                onCrearBitacora = { navController.navigate(Screen.CrearBitacora.route) }
            )
        }

        // 13. Recordatorios (Docente)
        composable(Screen.Recordatorios.route) {
            RecordatoriosScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 14. Perfil (Común)
        composable(
            route = Screen.Perfil.route,
            arguments = listOf(navArgument("rol") { type = NavType.StringType })
        ) { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rol") ?: "docente"
            PerfilScreen(
                rol = rol,
                onNavigateBack = { navController.popBackStack() },
                onCerrarSesion = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
