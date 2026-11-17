package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import cl.duoc.data.MockData

/**
 * Pantalla de historial de bitácoras de meses anteriores para el docente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialBitacorasScreen(
    onNavigateBack: () -> Unit,
    onVerBitacora: (String) -> Unit,
    onCrearBitacora: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Diálogo demostrativo de descarga de reporte
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Informe") },
            text = { Text("Informe descargado demostrativamente.") },
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
                title = { Text("Historial de Bitácoras") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { mostrarDialogo = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Descargar Informe Consolidado (Demo)")
            }

            OutlinedButton(
                onClick = onCrearBitacora,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Nueva Bitácora")
            }

            Text("Bitácoras Anteriores y Actuales:", fontWeight = FontWeight.Bold)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(MockData.bitacoras) { bitacora ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(bitacora.titulo, fontWeight = FontWeight.Bold)
                            Text("${bitacora.asignatura} - ${bitacora.curso}")
                            Text("Periodo: ${bitacora.mes} ${bitacora.anio} • Estado: ${bitacora.estado}")

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { onVerBitacora(bitacora.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver Detalle")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistorialBitacorasScreenPreview() {
    HistorialBitacorasScreen(
        onNavigateBack = {},
        onVerBitacora = {},
        onCrearBitacora = {}
    )
}
