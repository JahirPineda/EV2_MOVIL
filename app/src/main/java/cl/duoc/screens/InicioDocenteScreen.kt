package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.data.MockData

/**
 * Pantalla de referencia de inicio del docente (versión inicial con MockData).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioDocenteScreen(
    onCrearBitacora: () -> Unit,
    onVerBitacoras: () -> Unit,
    onVerSeguimiento: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerRecordatorios: () -> Unit,
    onVerPerfil: () -> Unit,
    onVerDetalleBitacora: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Docente") }
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
            Text(
                text = "Bienvenida, Profesora María González",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cursos a cargo: 6.º básico A (Matemáticas y Ciencias)",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Resumen del Mes:", fontWeight = FontWeight.Bold)
                    Text("• Bitácoras activas: 3")
                    Text("• Registros por revisar: 8")
                    Text("• Estudiantes sin participar: 3")
                    Text("• Registros revisados: 16")
                }
            }

            Button(
                onClick = onCrearBitacora,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Nueva Bitácora")
            }

            Text("Bitácoras Recientes:", fontWeight = FontWeight.Bold)

            MockData.bitacoras.take(2).forEach { bitacora ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(bitacora.titulo, fontWeight = FontWeight.Bold)
                        Text("${bitacora.asignatura} - ${bitacora.curso}")
                        Text("Estado: ${bitacora.estado}")
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = { onVerDetalleBitacora(bitacora.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver Detalle")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Opciones de Navegación:", fontWeight = FontWeight.Bold)

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
                Text("Mi Perfil / Cerrar Sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioDocenteScreenPreview() {
    InicioDocenteScreen(
        onCrearBitacora = {},
        onVerBitacoras = {},
        onVerSeguimiento = {},
        onVerHistorial = {},
        onVerRecordatorios = {},
        onVerPerfil = {},
        onVerDetalleBitacora = {}
    )
}
