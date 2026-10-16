package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import cl.duoc.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRegistroDocenteScreen(
    registroId: String,
    onNavigateBack: () -> Unit
) {
    val registro = MockData.registros.find { it.id == registroId } ?: MockData.registros.first()

    var estado by remember { mutableStateOf(registro.estado) }
    var comentario by remember { mutableStateOf(registro.comentarioDocente) }
    var nuevoComentario by remember { mutableStateOf("") }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var mensajeDialogo by remember { mutableStateOf("") }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Revisión") },
            text = { Text(mensajeDialogo) },
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
                title = { Text("Revisión de Aprendizaje") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Estudiante: ${registro.estudiante}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("${registro.curso} • ${registro.asignatura}")
            Text("Fecha de clase: ${registro.fecha} • Estado: $estado", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))

            Text("Contenidos trabajados:", fontWeight = FontWeight.Bold)
            Text(registro.contenidos)

            Text("Actividad realizada:", fontWeight = FontWeight.Bold)
            Text(registro.actividades)

            Text("Aprendizaje del estudiante:", fontWeight = FontWeight.Bold)
            Text(registro.aprendizajes)

            Text("Dudas o dificultades:", fontWeight = FontWeight.Bold)
            Text(registro.dificultades)

            if (registro.evidencias.isNotEmpty()) {
                Text("Evidencias:", fontWeight = FontWeight.Bold)
                registro.evidencias.forEach { e ->
                    Text("• $e")
                }
            }

            if (comentario.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Retroalimentación docente actual:", fontWeight = FontWeight.Bold)
                Text(comentario)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nuevoComentario,
                onValueChange = { nuevoComentario = it },
                label = { Text("Escribir comentario o retroalimentación") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nuevoComentario.isNotBlank()) {
                        comentario = nuevoComentario
                        nuevoComentario = ""
                        mensajeDialogo = "Comentario guardado correctamente."
                        mostrarDialogo = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Comentario")
            }

            OutlinedButton(
                onClick = {
                    estado = "Revisado"
                    mensajeDialogo = "Aporte marcado como 'Revisado'."
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Marcar como Revisado")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleRegistroDocenteScreenPreview() {
    DetalleRegistroDocenteScreen(
        registroId = "1",
        onNavigateBack = {}
    )
}
