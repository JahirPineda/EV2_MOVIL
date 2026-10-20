package cl.duoc.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.data.SessionManager
import cl.duoc.viewmodel.HistorialRegistrosViewModel
import coil.compose.rememberAsyncImagePainter

/**
 * Pantalla que muestra el historial de aportes y registros enviados por el estudiante.
 * Muestra el estado (PENDIENTE o REVISADO), fotos adjuntas y la retroalimentación del profesor.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialRegistrosScreen(
    viewModel: HistorialRegistrosViewModel,
    onNavigateBack: () -> Unit,
    onVerDetalleRegistro: (Long, Long) -> Unit
) {
    val registros by viewModel.registros.collectAsState()
    val usuarioActual by SessionManager.usuarioActual.collectAsState()

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
            Text(
                text = "Estudiante: ${usuarioActual?.nombre ?: "Estudiante"} (${registros.size} registros)",
                fontWeight = FontWeight.Bold
            )

            // Lista con todos los registros del alumno
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(registros) { reg ->
                    val esRevisado = reg.estado.equals("REVISADO", ignoreCase = true)
                    val colorEstado = if (esRevisado) Color(0xFF2E7D32) else Color(0xFFEF6C00)

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Encabezado: Bitácora ID y Estado
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Bitácora ID: #${reg.bitacoraId}", fontWeight = FontWeight.Bold)

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

                            Text("Fecha de clase: ${reg.fecha}")
                            Text("Observación: ${reg.observacion}")

                            // Miniatura de evidencia fotográfica si el alumno adjuntó una foto
                            if (!reg.fotoUri.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Attachment, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text("Evidencia adjunta", style = MaterialTheme.typography.bodySmall)
                                }
                                Image(
                                    painter = rememberAsyncImagePainter(model = reg.fotoUri),
                                    contentDescription = "Miniatura evidencia",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // Mensaje de retroalimentación dejado por el profesor
                            if (!reg.retroalimentacionDocente.isNullOrBlank()) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Feedback, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Column {
                                            Text(
                                                text = "Retroalimentación Docente:",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(reg.retroalimentacionDocente, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Botón para editar o ver detalle
                            Button(
                                onClick = { onVerDetalleRegistro(reg.bitacoraId, reg.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (esRevisado) "Ver Registro (Revisado)" else "Editar Registro")
                            }
                        }
                    }
                }
            }
        }
    }
}
