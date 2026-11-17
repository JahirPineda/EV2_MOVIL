package cl.duoc

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cl.duoc.navigation.BitacoraNavGraph
import cl.duoc.ui.theme.BitacoraTheme
import cl.duoc.ui.theme.FondoClaro

/**
 * Actividad principal de la aplicación.
 * Es el punto de entrada que inicializa Jetpack Compose, solicita permisos y carga la navegación.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño de pantalla completa de borde a borde
        enableEdgeToEdge()

        setContent {
            // Aplicamos el tema visual personalizado de la app
            BitacoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FondoClaro
                ) {
                    // Lanzador para solicitar permiso de notificaciones en Android 13 (Tiramisu) o superior
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { _ -> }

                    // Solicita el permiso una sola vez al abrir la aplicación
                    LaunchedEffect(Unit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }

                    // Controlador de navegación principal
                    val navController = rememberNavController()

                    // Cargamos el grafo de navegación con todas las pantallas
                    BitacoraNavGraph(navController = navController)
                }
            }
        }
    }
}
