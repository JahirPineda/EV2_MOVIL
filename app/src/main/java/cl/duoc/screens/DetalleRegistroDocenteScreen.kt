package cl.duoc.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.viewmodel.DetalleRegistroDocenteViewModel
import coil.compose.rememberAsyncImagePainter

/**
 * Pantalla que permite al docente inspeccionar el aporte de un estudiante
 * y agregar comentarios de retroalimentación o marcarlo como revisado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRegistroDocenteScreen(
    registroId: String,
    viewModel: DetalleRegistroDocenteViewModel,
    onNavigateBack: () -> Unit
) {
    val registro by viewModel.registro.collectAsState()
    var nuevoComentario by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var mensajeExito by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(mensajeExito) {
        mensajeExito?.let {
            snackbarHostState.showSnackbar(it)
            mensajeExito = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Revisión de Aprendizaje") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (registro == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val reg = registro!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Estudiante ID: ${reg.estudianteId}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Fecha de clase: ${reg.fecha} • Estado: ${reg.estado}", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(4.dp))

                Text("Observación del estudiante:", fontWeight = FontWeight.Bold)
                Text(reg.observacion)

                // Evidencia fotográfica si existe
                if (!reg.fotoUri.isNullOrBlank()) {
                    Text("Evidencia Fotográfica:", fontWeight = FontWeight.Bold)
                    Image(
                        painter = rememberAsyncImagePainter(model = reg.fotoUri),
                        contentDescription = "Evidencia",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Retroalimentación actual
                if (!reg.retroalimentacionDocente.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Retroalimentación docente actual:", fontWeight = FontWeight.Bold)
                    Text(reg.retroalimentacionDocente)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para escribir nuevo comentario
                OutlinedTextField(
                    value = nuevoComentario,
                    onValueChange = { nuevoComentario = it },
                    label = { Text("Escribir comentario o retroalimentación") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                // Botón para solo guardar el comentario
                Button(
                    onClick = {
                        if (nuevoComentario.isNotBlank()) {
                            viewModel.actualizarRetroalimentacionYEstado(nuevoComentario.trim(), reg.estado) {
                                nuevoComentario = ""
                                mensajeExito = "¡Comentario guardado correctamente!"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Comentario")
                }

                // Botón para marcar el registro como REVISADO
                OutlinedButton(
                    onClick = {
                        val comentarioFinal = if (nuevoComentario.isNotBlank()) {
                            nuevoComentario.trim()
                        } else {
                            reg.retroalimentacionDocente ?: "Revisado por el docente."
                        }

                        viewModel.actualizarRetroalimentacionYEstado(comentarioFinal, "REVISADO") {
                            nuevoComentario = ""
                            mensajeExito = "¡Aporte marcado como 'REVISADO'!"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como Revisado")
                }
            }
        }
    }
}
