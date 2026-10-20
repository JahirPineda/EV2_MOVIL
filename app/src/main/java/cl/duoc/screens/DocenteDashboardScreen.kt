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
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import cl.duoc.data.SessionManager
import cl.duoc.ui.components.LogoColegio

/**
 * Pantalla de inicio para el Docente.
 * Muestra el resumen del usuario conectado y accesos directos a todas sus funciones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocenteDashboardScreen(
    onCrearBitacora: () -> Unit,
    onVerBitacoras: () -> Unit,
    onVerSeguimiento: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerRecordatorios: () -> Unit,
    onVerPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    // Obtenemos los datos del docente conectado desde el SessionManager
    val usuarioActual by SessionManager.usuarioActual.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Docente (Dashboard)") }
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
                        text = "Bienvenida, ${usuarioActual?.nombre ?: "Docente"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "Curso asignado: ${usuarioActual?.cursoAsignado ?: "6.º básico A"} • Asignatura: ${usuarioActual?.asignaturaAsignada ?: "Matemáticas"}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Tarjeta de resumen
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Resumen del Periodo:", fontWeight = FontWeight.Bold)
                    Text("• Rol: DOCENTE")
                    Text("• Estado: ${if (usuarioActual != null) "Activo" else "Inactivo"}")
                }
            }

            // Botones de acción principales
            Button(
                onClick = onCrearBitacora,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Nueva Bitácora")
            }

            Button(onClick = onVerBitacoras, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Todas las Bitácoras")
            }

            Button(onClick = onVerSeguimiento, modifier = Modifier.fillMaxWidth()) {
                Text("Seguimiento de Estudiantes")
            }

            Button(onClick = onVerHistorial, modifier = Modifier.fillMaxWidth()) {
                Text("Historial de Meses Anteriores")
            }

            Button(onClick = onVerRecordatorios, modifier = Modifier.fillMaxWidth()) {
                Text("Recordatorios")
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
fun DocenteDashboardScreenPreview() {
    DocenteDashboardScreen(
        onCrearBitacora = {},
        onVerBitacoras = {},
        onVerSeguimiento = {},
        onVerHistorial = {},
        onVerRecordatorios = {},
        onVerPerfil = {},
        onCerrarSesion = {}
    )
}
