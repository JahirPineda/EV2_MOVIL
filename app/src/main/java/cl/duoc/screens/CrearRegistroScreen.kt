package cl.duoc.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.data.SessionManager
import cl.duoc.viewmodel.RegistroViewModel
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream

/**
 * Pantalla que permite al estudiante registrar o editar un aporte de aprendizaje,
 * incluyendo evidencia fotográfica obtenida desde la cámara o la galería del dispositivo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRegistroScreen(
    bitacoraId: Long,
    registroId: Long?,
    viewModel: RegistroViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val usuarioActual by SessionManager.usuarioActual.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val esEdicion = registroId != null && registroId > 0L
    val tituloPantalla = if (esEdicion) "Editar Registro" else "Registrar Avance"
    val textoBoton = if (esEdicion) "Actualizar Registro" else "Enviar Registro"

    // 1. Lanzador nativo para seleccionar imagen desde la Galería
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onFotoUriChanged(uri.toString())
        }
    }

    // 2. Lanzador nativo para tomar una foto directamente con la Cámara
    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { fotoBitmap: Bitmap? ->
        if (fotoBitmap != null) {
            try {
                // Guardamos la foto capturada en el almacenamiento temporal de la app
                val archivoFoto = File(context.cacheDir, "foto_${System.currentTimeMillis()}.jpg")
                val stream = FileOutputStream(archivoFoto)
                fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                stream.flush()
                stream.close()

                val uriFoto = Uri.fromFile(archivoFoto)
                viewModel.onFotoUriChanged(uriFoto.toString())
            } catch (e: Exception) {
                // Si ocurre un problema al guardar, se ignora silenciosamente
            }
        }
    }

    // Muestra mensajes de confirmación o error
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
            // Aviso si el registro ya fue revisado y aprobado por el docente
            if (uiState.esRevisado) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text(
                            text = "Este registro ya ha sido marcado como 'REVISADO' por el docente y no puede ser modificado.",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = if (esEdicion) "Detalle / Edición de Registro" else "Nuevo Registro de Aprendizaje",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            // Campo: Fecha
            OutlinedTextField(
                value = uiState.fecha,
                onValueChange = { viewModel.onFechaChanged(it) },
                label = { Text("Fecha de la clase *") },
                enabled = !uiState.esRevisado,
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

            // Campo: Observación / Aprendizaje
            OutlinedTextField(
                value = uiState.observacion,
                onValueChange = { viewModel.onObservacionChanged(it) },
                label = { Text("Observación / Avance / Dificultad *") },
                enabled = !uiState.esRevisado,
                isError = uiState.observacionError != null,
                trailingIcon = {
                    if (uiState.observacionError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (uiState.observacionError != null) {
                        Text(uiState.observacionError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            // Botones de Galería y Cámara (Recursos Nativos)
            if (!uiState.esRevisado) {
                Text("Evidencia Fotográfica (Nativo):", fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            launcherGaleria.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("Galería")
                    }

                    OutlinedButton(
                        onClick = {
                            launcherCamara.launch(null)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("Cámara")
                    }
                }
            }

            // Vista previa de la foto seleccionada
            AnimatedVisibility(
                visible = uiState.fotoUri != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Vista previa de evidencia:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (uiState.fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = uiState.fotoUri),
                            contentDescription = "Vista previa",
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (!uiState.esRevisado) {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(onClick = { viewModel.onFotoUriChanged(null) }) {
                            Text("Eliminar foto")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón de Enviar o Actualizar
            if (!uiState.esRevisado) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = {
                            val estudianteId = usuarioActual?.id ?: "33.333.333-3"
                            viewModel.guardarOActualizarRegistro(bitacoraId, estudianteId) {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(textoBoton)
                    }
                }
            }
        }
    }
}
