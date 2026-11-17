package cl.duoc.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cl.duoc.data.AppDatabase
import cl.duoc.data.SessionManager
import cl.duoc.data.repository.AppRepository
import cl.duoc.screens.CrearEditarBitacoraScreen
import cl.duoc.screens.CrearRegistroScreen
import cl.duoc.screens.DetalleBitacoraScreen
import cl.duoc.screens.DetalleRegistroDocenteScreen
import cl.duoc.screens.DocenteDashboardScreen
import cl.duoc.screens.EstudianteDashboardScreen
import cl.duoc.screens.HistorialBitacorasScreen
import cl.duoc.screens.HistorialRegistrosScreen
import cl.duoc.screens.ListaBitacorasScreen
import cl.duoc.screens.LoginScreen
import cl.duoc.screens.PerfilScreen
import cl.duoc.screens.RecordatoriosScreen
import cl.duoc.screens.RegistroUsuarioScreen
import cl.duoc.screens.RevisionDocenteScreen
import cl.duoc.screens.SeguimientoScreen
import cl.duoc.viewmodel.BitacoraDetailViewModel
import cl.duoc.viewmodel.BitacoraViewModel
import cl.duoc.viewmodel.BitacorasListViewModel
import cl.duoc.viewmodel.DetalleRegistroDocenteViewModel
import cl.duoc.viewmodel.HistorialRegistrosViewModel
import cl.duoc.viewmodel.LoginViewModel
import cl.duoc.viewmodel.PerfilViewModel
import cl.duoc.viewmodel.RegisterViewModel
import cl.duoc.viewmodel.RegistroViewModel
import cl.duoc.viewmodel.RevisionDocenteViewModel
import cl.duoc.viewmodel.SeguimientoViewModel

/**
 * Grafo principal de navegación con Jetpack Compose.
 * Conecta cada ruta con su pantalla correspondiente y le proporciona su ViewModel y dependencias.
 */
@Composable
fun BitacoraNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Creamos el repositorio una sola vez usando 'remember' para compartirlo en todas las pantallas
    val repository = remember {
        val database = AppDatabase.getDatabase(context)
        AppRepository(database.usuarioDao(), database.bitacoraDao(), database.registroDao())
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // -------------------------------------------------------------
        // 1. PANTALLA DE LOGIN
        // -------------------------------------------------------------
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.Factory(application, repository)
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { rol ->
                    // Según el rol, dirigimos al panel correspondiente
                    if (rol.equals("DOCENTE", ignoreCase = true)) {
                        navController.navigate(Screen.DocenteDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.EstudianteDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // -------------------------------------------------------------
        // 2. REGISTRO DE NUEVO USUARIO
        // -------------------------------------------------------------
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModel.Factory(repository)
            )

            RegistroUsuarioScreen(
                viewModel = registerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // 3. PANEL PRINCIPAL DOCENTE
        // -------------------------------------------------------------
        composable(Screen.DocenteDashboard.route) {
            val usuario by SessionManager.usuarioActual.collectAsState()
            // Control de acceso: si no es docente, redirige a su panel
            if (usuario?.rol == "ESTUDIANTE") {
                navController.navigate(Screen.EstudianteDashboard.route) {
                    popUpTo(Screen.DocenteDashboard.route) { inclusive = true }
                }
                return@composable
            }

            DocenteDashboardScreen(
                onCrearBitacora = { navController.navigate(Screen.CrearBitacora.createRoute(null)) },
                onVerBitacoras = { navController.navigate(Screen.ListaBitacoras.createRoute("docente")) },
                onVerSeguimiento = { navController.navigate(Screen.Seguimiento.route) },
                onVerHistorial = { navController.navigate(Screen.HistorialBitacoras.route) },
                onVerRecordatorios = { navController.navigate(Screen.Recordatorios.route) },
                onVerPerfil = { navController.navigate(Screen.Perfil.createRoute("docente")) },
                onCerrarSesion = {
                    SessionManager.cerrarSesion()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // -------------------------------------------------------------
        // 4. PANEL PRINCIPAL ESTUDIANTE
        // -------------------------------------------------------------
        composable(Screen.EstudianteDashboard.route) {
            EstudianteDashboardScreen(
                onRegistrarAprendizaje = { bitacoraId ->
                    val idLong = bitacoraId.toLongOrNull() ?: 1L
                    navController.navigate(Screen.Registro.createRoute(idLong, null))
                },
                onVerBitacoras = { navController.navigate(Screen.ListaBitacoras.createRoute("estudiante")) },
                onVerHistorial = { navController.navigate(Screen.HistorialRegistros.route) },
                onVerPerfil = { navController.navigate(Screen.Perfil.createRoute("estudiante")) },
                onCerrarSesion = {
                    SessionManager.cerrarSesion()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // -------------------------------------------------------------
        // 5. LISTA DE TODAS LAS BITÁCORAS (Room)
        // -------------------------------------------------------------
        composable(
            route = Screen.ListaBitacoras.route,
            arguments = listOf(navArgument("rol") { type = NavType.StringType })
        ) { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rol") ?: "estudiante"
            val listViewModel: BitacorasListViewModel = viewModel(
                factory = BitacorasListViewModel.Factory(repository)
            )

            ListaBitacorasScreen(
                rol = rol,
                viewModel = listViewModel,
                onNavigateBack = { navController.popBackStack() },
                onVerDetalleBitacora = { id ->
                    navController.navigate(Screen.DetalleBitacora.createRoute(id, rol))
                }
            )
        }

        // -------------------------------------------------------------
        // 6. CREAR O EDITAR BITÁCORA (Docente)
        // -------------------------------------------------------------
        composable(
            route = Screen.CrearBitacora.route,
            arguments = listOf(navArgument("bitacoraId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val usuario by SessionManager.usuarioActual.collectAsState()
            if (usuario?.rol == "ESTUDIANTE") {
                navController.popBackStack()
                return@composable
            }

            val bIdArg = backStackEntry.arguments?.getLong("bitacoraId") ?: -1L
            val bitacoraId = if (bIdArg > 0L) bIdArg else null

            val bitacoraViewModel: BitacoraViewModel = viewModel(
                factory = BitacoraViewModel.Factory(application, repository, bitacoraId)
            )

            CrearEditarBitacoraScreen(
                bitacoraId = bitacoraId,
                viewModel = bitacoraViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // 7. DETALLE DE BITÁCORA
        // -------------------------------------------------------------
        composable(
            route = Screen.DetalleBitacora.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id") ?: "1"
            val idLong = idStr.toLongOrNull() ?: 1L
            val rol = backStackEntry.arguments?.getString("rol") ?: "estudiante"

            val detailViewModel: BitacoraDetailViewModel = viewModel(
                factory = BitacoraDetailViewModel.Factory(repository, idLong)
            )

            DetalleBitacoraScreen(
                bitacoraId = idStr,
                rol = rol,
                viewModel = detailViewModel,
                onNavigateBack = { navController.popBackStack() },
                onEditarBitacora = { bId ->
                    navController.navigate(Screen.CrearBitacora.createRoute(bId.toLongOrNull()))
                },
                onRegistrarAprendizaje = { bId ->
                    navController.navigate(Screen.Registro.createRoute(bId.toLongOrNull() ?: 1L, null))
                },
                onRevisarAportes = { bId ->
                    navController.navigate(Screen.RevisionDocente.createRoute(bId))
                },
                onVerMisRegistros = {
                    navController.navigate(Screen.HistorialRegistros.route)
                }
            )
        }

        // -------------------------------------------------------------
        // 8. REGISTRO DE APRENDIZAJE (Creación o Edición - Estudiante)
        // -------------------------------------------------------------
        composable(
            route = Screen.Registro.route,
            arguments = listOf(
                navArgument("bitacoraId") { type = NavType.StringType },
                navArgument("registroId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val bitacoraIdStr = backStackEntry.arguments?.getString("bitacoraId") ?: "1"
            val bitacoraId = bitacoraIdStr.toLongOrNull() ?: 1L
            val rIdArg = backStackEntry.arguments?.getLong("registroId") ?: -1L
            val registroId = if (rIdArg > 0L) rIdArg else null

            val registroViewModel: RegistroViewModel = viewModel(
                factory = RegistroViewModel.Factory(repository, registroId)
            )

            CrearRegistroScreen(
                bitacoraId = bitacoraId,
                registroId = registroId,
                viewModel = registroViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // 9. HISTORIAL DE APORTES (Estudiante)
        // -------------------------------------------------------------
        composable(Screen.HistorialRegistros.route) {
            val usuario by SessionManager.usuarioActual.collectAsState()
            val estudianteId = usuario?.id ?: "33.333.333-3"

            val historialViewModel: HistorialRegistrosViewModel = viewModel(
                factory = HistorialRegistrosViewModel.Factory(repository, estudianteId)
            )

            HistorialRegistrosScreen(
                viewModel = historialViewModel,
                onNavigateBack = { navController.popBackStack() },
                onVerDetalleRegistro = { bId, regId ->
                    navController.navigate(Screen.Registro.createRoute(bId, regId))
                }
            )
        }

        // -------------------------------------------------------------
        // 10. REVISIÓN DE APORTES (Docente)
        // -------------------------------------------------------------
        composable(
            route = Screen.RevisionDocente.route,
            arguments = listOf(navArgument("bitacoraId") { type = NavType.StringType })
        ) { backStackEntry ->
            val usuario by SessionManager.usuarioActual.collectAsState()
            if (usuario?.rol == "ESTUDIANTE") {
                navController.popBackStack()
                return@composable
            }

            val bitacoraIdStr = backStackEntry.arguments?.getString("bitacoraId") ?: "1"
            val bitacoraId = bitacoraIdStr.toLongOrNull() ?: 1L

            val revisionViewModel: RevisionDocenteViewModel = viewModel(
                factory = RevisionDocenteViewModel.Factory(repository, bitacoraId)
            )

            RevisionDocenteScreen(
                bitacoraId = bitacoraIdStr,
                viewModel = revisionViewModel,
                onNavigateBack = { navController.popBackStack() },
                onVerDetalleRegistro = { regId ->
                    navController.navigate(Screen.DetalleRegistroDocente.createRoute(regId))
                }
            )
        }

        // -------------------------------------------------------------
        // 11. DETALLE DEL REGISTRO PARA RETROALIMENTACIÓN (Docente)
        // -------------------------------------------------------------
        composable(
            route = Screen.DetalleRegistroDocente.route,
            arguments = listOf(navArgument("registroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val registroIdStr = backStackEntry.arguments?.getString("registroId") ?: "1"
            val registroId = registroIdStr.toLongOrNull() ?: 1L

            val detalleRegistroViewModel: DetalleRegistroDocenteViewModel = viewModel(
                factory = DetalleRegistroDocenteViewModel.Factory(repository, registroId)
            )

            DetalleRegistroDocenteScreen(
                registroId = registroIdStr,
                viewModel = detalleRegistroViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // 12. SEGUIMIENTO DE PARTICIPACIÓN DEL CURSO (Docente)
        // -------------------------------------------------------------
        composable(Screen.Seguimiento.route) {
            val usuario by SessionManager.usuarioActual.collectAsState()
            if (usuario?.rol == "ESTUDIANTE") {
                navController.popBackStack()
                return@composable
            }

            val seguimientoViewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModel.Factory(repository)
            )

            SeguimientoScreen(
                viewModel = seguimientoViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // 13. HISTORIAL DE BITÁCORAS ANTERIORES (Docente)
        // -------------------------------------------------------------
        composable(Screen.HistorialBitacoras.route) {
            val usuario by SessionManager.usuarioActual.collectAsState()
            if (usuario?.rol == "ESTUDIANTE") {
                navController.popBackStack()
                return@composable
            }

            HistorialBitacorasScreen(
                onNavigateBack = { navController.popBackStack() },
                onVerBitacora = { id ->
                    navController.navigate(Screen.DetalleBitacora.createRoute(id, "docente"))
                },
                onCrearBitacora = { navController.navigate(Screen.CrearBitacora.createRoute(null)) }
            )
        }

        // -------------------------------------------------------------
        // 14. RECORDATORIOS (Docente)
        // -------------------------------------------------------------
        composable(Screen.Recordatorios.route) {
            val usuario by SessionManager.usuarioActual.collectAsState()
            if (usuario?.rol == "ESTUDIANTE") {
                navController.popBackStack()
                return@composable
            }

            RecordatoriosScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // 15. PERFIL DE USUARIO (Docente y Estudiante)
        // -------------------------------------------------------------
        composable(
            route = Screen.Perfil.route,
            arguments = listOf(navArgument("rol") { type = NavType.StringType })
        ) { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rol") ?: "docente"

            val perfilViewModel: PerfilViewModel = viewModel(
                factory = PerfilViewModel.Factory(repository)
            )

            PerfilScreen(
                rol = rol,
                viewModel = perfilViewModel,
                onNavigateBack = { navController.popBackStack() },
                onCerrarSesion = {
                    SessionManager.cerrarSesion()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
