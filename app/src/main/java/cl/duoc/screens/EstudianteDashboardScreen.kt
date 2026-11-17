package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import cl.duoc.data.SessionManager
import cl.duoc.ui.components.LogoColegio

/**
 * Pantalla de inicio para el Estudiante.
 * Permite registrar aprendizajes y revisar aportes realizados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteDashboardScreen(
    onRegistrarAprendizaje: (String) -> Unit,
    onVerBitacoras: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    // Obtenemos los datos del estudiante conectado
    val usuarioActual by SessionManager.usuarioActual.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Estudiante (Dashboard)") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Encabezado institucional con logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LogoColegio(size = 56.dp, mostrarTexto = false)
                Column {
                    Text(
                        text = "Escuela Básica de San Bernardo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Bienvenido, ${usuarioActual?.nombre ?: "Estudiante"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "Curso: ${usuarioActual?.cursoAsignado ?: "6.º básico A"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Mis Aportes:", fontWeight = FontWeight.Bold)
                    Text("• Rol: ESTUDIANTE")
                    Text("• Estado: ${if (usuarioActual != null) "Activo" else "Inactivo"}")
                }
            }

            // Botones de acción principales
            Button(
                onClick = { onRegistrarAprendizaje("1") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Nuevo Aprendizaje")
            }

            Button(onClick = onVerBitacoras, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Bitácoras Disponibles")
            }

            Button(onClick = onVerHistorial, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Mis Aportes Anteriores")
            }

            OutlinedButton(onClick = onVerPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Mi Perfil")
            }

            OutlinedButton(
                onClick = {
                    SessionManager.cerrarSesion()
                    onCerrarSesion()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión (Logout)")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EstudianteDashboardScreenPreview() {
    EstudianteDashboardScreen(
        onRegistrarAprendizaje = {},
        onVerBitacoras = {},
        onVerHistorial = {},
        onVerPerfil = {},
        onCerrarSesion = {}
    )
}
