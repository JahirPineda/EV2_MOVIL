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
 * Pantalla de referencia de inicio del estudiante (versión inicial con MockData).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioEstudianteScreen(
    onRegistrarAprendizaje: (String) -> Unit,
    onVerBitacoras: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerPerfil: () -> Unit,
    onVerDetalleBitacora: (String) -> Unit
) {
    val bitacoras = MockData.bitacoras.filter { it.curso == "6.º básico A" && it.estado == "Publicada" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Estudiante") }
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
                text = "Bienvenido, Matías Rojas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Curso: 6.º básico A • Escuela San Bernardo",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Mis Aportes:", fontWeight = FontWeight.Bold)
                    Text("• Registros enviados en el mes: 2")
                    Text("• Registros revisados por la profesora: 1")
                }
            }

            Button(
                onClick = { onRegistrarAprendizaje("1") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Nuevo Aprendizaje")
            }

            Text("Bitácoras Disponibles:", fontWeight = FontWeight.Bold)

            bitacoras.forEach { bitacora ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(bitacora.titulo, fontWeight = FontWeight.Bold)
                        Text("${bitacora.asignatura} - Docente: ${bitacora.docente}")
                        Text("Periodo: ${bitacora.mes} ${bitacora.anio}")
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = { onVerDetalleBitacora(bitacora.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver Bitácora")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Otras Opciones:", fontWeight = FontWeight.Bold)

            Button(onClick = onVerHistorial, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Mis Aportes Anteriores")
            }

            OutlinedButton(onClick = onVerPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Mi Perfil / Cerrar Sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioEstudianteScreenPreview() {
    InicioEstudianteScreen(
        onRegistrarAprendizaje = {},
        onVerBitacoras = {},
        onVerHistorial = {},
        onVerPerfil = {},
        onVerDetalleBitacora = {}
    )
}
