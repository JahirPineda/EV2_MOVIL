package cl.duoc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.viewmodel.RevisionDocenteViewModel

/**
 * Pantalla que permite al docente revisar todos los registros de los alumnos
 * para una bitácora determinada y filtrarlos por estado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionDocenteScreen(
    bitacoraId: String,
    viewModel: RevisionDocenteViewModel,
    onNavigateBack: () -> Unit,
    onVerDetalleRegistro: (String) -> Unit
) {
    val bitacora by viewModel.bitacora.collectAsState()
    val registros by viewModel.registros.collectAsState()
    var filtro by remember { mutableStateOf("Todos") }

    // Filtramos la lista según el botón seleccionado ("Todos", "PENDIENTE", "REVISADO")
    val registrosFiltrados = registros.filter { reg ->
        if (filtro.equals("Todos", ignoreCase = true)) true
        else reg.estado.equals(filtro, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Revisión de Aportes") },
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
            Text(bitacora?.titulo ?: "Cargando bitácora...", fontWeight = FontWeight.Bold)
            Text("Fecha: ${bitacora?.fecha ?: ""}")

            // Botones de filtro: Todos / Pendiente / Revisado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todos", "PENDIENTE", "REVISADO").forEach { opcion ->
                    val textoEtiqueta = when (opcion) {
                        "PENDIENTE" -> "Pendiente"
                        "REVISADO" -> "Revisado"
                        else -> "Todos"
                    }

                    if (filtro.equals(opcion, ignoreCase = true)) {
                        Button(onClick = { filtro = opcion }, modifier = Modifier.weight(1f)) {
                            Text(textoEtiqueta)
                        }
                    } else {
                        OutlinedButton(onClick = { filtro = opcion }, modifier = Modifier.weight(1f)) {
                            Text(textoEtiqueta)
                        }
                    }
                }
            }

            Text(
                text = "Mostrando ${registrosFiltrados.size} registros",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            // Lista de aportes de los estudiantes
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(registrosFiltrados) { reg ->
                    val colorEstado = if (reg.estado.equals("REVISADO", ignoreCase = true)) Color(0xFF2E7D32) else Color(0xFFEF6C00)

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Estudiante ID: ${reg.estudianteId}", fontWeight = FontWeight.Bold)

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(colorEstado, CircleShape)
                                    )
                                    Text(
                                        text = reg.estado,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Text("Fecha: ${reg.fecha}")
                            Text("Observación: ${reg.observacion}")

                            if (!reg.retroalimentacionDocente.isNullOrBlank()) {
                                Text(
                                    text = "Comentario docente: ${reg.retroalimentacionDocente}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { onVerDetalleRegistro(reg.id.toString()) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Revisar y Retroalimentar")
                            }
                        }
                    }
                }
            }
        }
    }
}
