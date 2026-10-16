package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.data.MockData
import cl.duoc.model.Recordatorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatoriosScreen(
    onNavigateBack: () -> Unit
) {
    val recordatorios = remember {
        mutableStateListOf<Recordatorio>().apply {
            addAll(MockData.recordatorios)
        }
    }

    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevaFecha by remember { mutableStateOf("05/11/2026") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recordatorios") },
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
            Text("Agregar Recordatorio:", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = nuevoTitulo,
                onValueChange = { nuevoTitulo = it },
                label = { Text("Título del aviso") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nuevaFecha,
                onValueChange = { nuevaFecha = it },
                label = { Text("Fecha límite") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nuevoTitulo.isNotBlank()) {
                        recordatorios.add(
                            0,
                            Recordatorio(
                                id = "${System.currentTimeMillis()}",
                                titulo = nuevoTitulo,
                                fecha = nuevaFecha,
                                descripcion = "",
                                completado = false
                            )
                        )
                        nuevoTitulo = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar a la Lista")
            }

            Text("Lista de Avisos:", fontWeight = FontWeight.Bold)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(recordatorios) { index, rec ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(rec.titulo, fontWeight = FontWeight.Bold)
                                Text("Fecha: ${rec.fecha}")
                            }

                            if (!rec.completado) {
                                TextButton(
                                    onClick = {
                                        recordatorios[index] = rec.copy(completado = true)
                                    }
                                ) {
                                    Text("Listo")
                                }
                            } else {
                                Text("✓ Completado")
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
fun RecordatoriosScreenPreview() {
    RecordatoriosScreen(onNavigateBack = {})
}
