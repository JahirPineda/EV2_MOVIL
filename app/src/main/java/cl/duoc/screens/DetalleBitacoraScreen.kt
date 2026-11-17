package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.viewmodel.BitacoraDetailViewModel

/**
 * Pantalla para ver el detalle de una bitácora específica.
 * Muestra distintas opciones según si quien la mira es Docente o Estudiante.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleBitacoraScreen(
    bitacoraId: String,
    rol: String,
    viewModel: BitacoraDetailViewModel,
    onNavigateBack: () -> Unit,
    onEditarBitacora: (String) -> Unit,
    onRegistrarAprendizaje: (String) -> Unit,
    onRevisarAportes: (String) -> Unit,
    onVerMisRegistros: () -> Unit
) {
    val bitacora by viewModel.bitacora.collectAsState()
    var mostrarDialogoCerrada by remember { mutableStateOf(false) }

    // Diálogo informativo al cerrar la bitácora
    if (mostrarDialogoCerrada) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrada = false },
            title = { Text("Bitácora Cerrada") },
            text = { Text("La bitácora se ha cerrado exitosamente en Room.") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoCerrada = false }) {
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
        if (bitacora == null) {
            // Indicador de carga mientras se consulta la base de datos
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val b = bitacora!!
            val esDocente = rol.equals("docente", ignoreCase = true) || rol.equals("DOCENTE", ignoreCase = true)
            val estaCerrada = b.estado.equals("CERRADA", ignoreCase = true)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(b.titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Fecha: ${b.fecha}")
                Text("Docente ID: ${b.docenteId}")
                Text("Estado: ${b.estado}")

                Spacer(modifier = Modifier.height(6.dp))

                Text("Descripción / Propósito:", fontWeight = FontWeight.Bold)
                Text(if (b.descripcion.isNotBlank()) b.descripcion else "Sin descripción especificada.")

                Spacer(modifier = Modifier.height(16.dp))

                // Opciones exclusivas para Docentes
                if (esDocente) {
                    Button(
                        onClick = { onEditarBitacora(b.id.toString()) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Editar Bitácora")
                    }

                    Button(
                        onClick = { onRevisarAportes(b.id.toString()) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Revisar Aportes de Estudiantes")
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.actualizarEstado("CERRADA") {
                                mostrarDialogoCerrada = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !estaCerrada
                    ) {
                        Text(if (estaCerrada) "Bitácora ya Cerrada" else "Cerrar Bitácora")
                    }
                } else {
                    // Opciones para Estudiantes
                    Button(
                        onClick = { onRegistrarAprendizaje(b.id.toString()) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !estaCerrada
                    ) {
                        Text(if (estaCerrada) "Bitácora Cerrada (No disponible)" else "Registrar Mi Aprendizaje")
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
}
