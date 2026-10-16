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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleBitacoraScreen(
    bitacoraId: String,
    rol: String,
    onNavigateBack: () -> Unit,
    onRegistrarAprendizaje: (String) -> Unit,
    onRevisarAportes: (String) -> Unit,
    onVerMisRegistros: () -> Unit
) {
    val bitacora = MockData.bitacoras.find { it.id == bitacoraId } ?: MockData.bitacoras.first()
    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Bitácora") },
            text = { Text("La bitácora se ha cerrado demostrativamente.") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Bitácora") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(bitacora.titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Asignatura: ${bitacora.asignatura}")
            Text("Curso: ${bitacora.curso}")
            Text("Periodo: ${bitacora.mes} ${bitacora.anio}")
            Text("Docente: ${bitacora.docente}")
            Text("Estado: ${bitacora.estado}")

            Spacer(modifier = Modifier.height(6.dp))

            Text("Propósito General:", fontWeight = FontWeight.Bold)
            Text(if (bitacora.proposito.isNotBlank()) bitacora.proposito else "Sin propósito especificado.")

            Spacer(modifier = Modifier.height(4.dp))

            Text("Instrucciones:", fontWeight = FontWeight.Bold)
            Text(if (bitacora.instrucciones.isNotBlank()) bitacora.instrucciones else "Sin instrucciones.")

            if (bitacora.preguntasOrientadoras.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Preguntas Orientadoras:", fontWeight = FontWeight.Bold)
                bitacora.preguntasOrientadoras.forEachIndexed { i, p ->
                    Text("${i + 1}. $p")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (rol == "docente") {
                Button(
                    onClick = { onRevisarAportes(bitacora.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Revisar Aportes de Estudiantes")
                }

                OutlinedButton(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Bitácora")
                }
            } else {
                Button(
                    onClick = { onRegistrarAprendizaje(bitacora.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Mi Aprendizaje")
                }

                OutlinedButton(
                    onClick = onVerMisRegistros,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Mis Aportes Anteriores")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleBitacoraScreenPreview() {
    DetalleBitacoraScreen(
        bitacoraId = "1",
        rol = "docente",
        onNavigateBack = {},
        onRegistrarAprendizaje = {},
        onRevisarAportes = {},
        onVerMisRegistros = {}
    )
}
