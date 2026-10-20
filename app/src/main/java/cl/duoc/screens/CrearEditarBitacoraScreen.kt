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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.data.SessionManager
import cl.duoc.ui.theme.GrisBorrador
import cl.duoc.ui.theme.RojoError
import cl.duoc.ui.theme.VerdeExito
import cl.duoc.viewmodel.BitacoraViewModel

/**
 * Pantalla que permite al docente crear una nueva bitácora o editar una existente en Room.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEditarBitacoraScreen(
    bitacoraId: Long?,
    viewModel: BitacoraViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val usuarioActual by SessionManager.usuarioActual.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val esEdicion = bitacoraId != null && bitacoraId > 0L
    val tituloPantalla = if (esEdicion) "Editar Bitácora" else "Crear Bitácora"

    // Notificaciones de éxito o error
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let { snackbarHostState.showSnackbar(it) }
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloPantalla) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Formulario de Bitácora (Docente)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            // Campo: Título
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = { viewModel.onTituloChanged(it) },
                label = { Text("Título *") },
                isError = uiState.tituloError != null,
                trailingIcon = {
                    if (uiState.tituloError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (uiState.tituloError != null) {
                        Text(uiState.tituloError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo: Descripción
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChanged(it) },
                label = { Text("Descripción / Propósito *") },
                isError = uiState.descripcionError != null,
                trailingIcon = {
                    if (uiState.descripcionError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (uiState.descripcionError != null) {
                        Text(uiState.descripcionError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Campo: Fecha
            OutlinedTextField(
                value = uiState.fecha,
                onValueChange = { viewModel.onFechaChanged(it) },
                label = { Text("Fecha (ej. 24/10/2026) *") },
                isError = uiState.fechaError != null,
                trailingIcon = {
                    if (uiState.fechaError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (uiState.fechaError != null) {
                        Text(uiState.fechaError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Selector de Estado con chips (BORRADOR, PUBLICADA, CERRADA)
            Text("Estado de la Bitácora:", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val estados = listOf("BORRADOR", "PUBLICADA", "CERRADA")
                estados.forEach { estado ->
                    val color = when (estado) {
                        "PUBLICADA" -> VerdeExito
                        "BORRADOR" -> GrisBorrador
                        "CERRADA" -> RojoError
                        else -> GrisBorrador
                    }

                    FilterChip(
                        selected = uiState.estado == estado,
                        onClick = { viewModel.onEstadoChanged(estado) },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(color, CircleShape)
                                )
                                Text(estado)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón para Guardar
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        val docenteId = usuarioActual?.id ?: "11.111.111-1"
                        viewModel.guardarBitacora(docenteId) {
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Bitácora")
                }
            }
        }
    }
}
