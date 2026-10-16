package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaBitacorasScreen(
    rol: String,
    onNavigateBack: () -> Unit,
    onVerDetalleBitacora: (String) -> Unit
) {
    var filtro by remember { mutableStateOf("Todas") }

    val bitacoras = MockData.bitacoras.filter {
        if (filtro == "Todas") true else it.asignatura.contains(filtro)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Bitácoras") },
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
            Text("Filtrar por Asignatura:", fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todas", "Matemáticas", "Ciencias").forEach { opcion ->
                    if (filtro == opcion) {
                        Button(
                            onClick = { filtro = opcion },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(opcion)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { filtro = opcion },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(opcion)
                        }
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bitacoras) { bitacora ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(bitacora.titulo, fontWeight = FontWeight.Bold)
                            Text("Asignatura: ${bitacora.asignatura}")
                            Text("Curso: ${bitacora.curso}")
                            Text("Mes: ${bitacora.mes} ${bitacora.anio}")
                            Text("Estado: ${bitacora.estado}")

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { onVerDetalleBitacora(bitacora.id) },
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
fun ListaBitacorasScreenPreview() {
    ListaBitacorasScreen(
        rol = "DOCENTE",
        onNavigateBack = {},
        onVerDetalleBitacora = {}
    )
}
