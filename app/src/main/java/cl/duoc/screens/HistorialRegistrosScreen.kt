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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialRegistrosScreen(
    onNavigateBack: () -> Unit,
    onVerDetalleRegistro: (String) -> Unit
) {
    val misRegistros = MockData.registros.filter { it.estudiante == "Matías Rojas" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Aportes Anteriores") },
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
            Text("Historial de registros personales:")

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(misRegistros) { reg ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${reg.asignatura} - ${reg.bitacoraTitulo}", fontWeight = FontWeight.Bold)
                            Text("Fecha de clase: ${reg.fecha}")
                            Text("Estado: ${reg.estado}")
                            Text("Aprendizaje: ${reg.aprendizajes}")

                            if (reg.comentarioDocente.isNotBlank()) {
                                Text("Comentario de la profesora: ${reg.comentarioDocente}", fontWeight = FontWeight.SemiBold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { onVerDetalleRegistro(reg.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver Registro Completo")
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
fun HistorialRegistrosScreenPreview() {
    HistorialRegistrosScreen(
        onNavigateBack = {},
        onVerDetalleRegistro = {}
    )
}
