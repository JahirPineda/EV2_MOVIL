package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearBitacoraScreen(
    onNavigateBack: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("6.º básico A") }
    var asignatura by remember { mutableStateOf("Matemáticas") }
    var mes by remember { mutableStateOf("Noviembre") }
    var proposito by remember { mutableStateOf("") }
    var instrucciones by remember { mutableStateOf("") }

    val preguntas = remember {
        mutableStateListOf(
            "¿Qué contenidos trabajamos hoy?",
            "¿Qué método o estrategia aprendiste en la clase?"
        )
    }
    var nuevaPregunta by remember { mutableStateOf("") }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var mensajeDialogo by remember { mutableStateOf("") }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                onNavigateBack()
            },
            title = { Text("Bitácora") },
            text = { Text(mensajeDialogo) },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        onNavigateBack()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Bitácora") },
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
            Text("Formulario de Creación", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título de la bitácora") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = curso,
                onValueChange = { curso = it },
                label = { Text("Curso") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = asignatura,
                onValueChange = { asignatura = it },
                label = { Text("Asignatura") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mes,
                onValueChange = { mes = it },
                label = { Text("Mes") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = proposito,
                onValueChange = { proposito = it },
                label = { Text("Propósito general") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = instrucciones,
                onValueChange = { instrucciones = it },
                label = { Text("Instrucciones") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Preguntas Orientadoras:", fontWeight = FontWeight.Bold)

            preguntas.forEachIndexed { i, p ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${i + 1}. $p", modifier = Modifier.weight(1f))
                    TextButton(onClick = { preguntas.removeAt(i) }) {
                        Text("Borrar")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nuevaPregunta,
                    onValueChange = { nuevaPregunta = it },
                    label = { Text("Nueva pregunta") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (nuevaPregunta.isNotBlank()) {
                            preguntas.add(nuevaPregunta.trim())
                            nuevaPregunta = ""
                        }
                    }
                ) {
                    Text("Agregar")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    mensajeDialogo = "¡Bitácora publicada con éxito!"
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar Bitácora")
            }

            OutlinedButton(
                onClick = {
                    mensajeDialogo = "Borrador guardado."
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Borrador")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CrearBitacoraScreenPreview() {
    CrearBitacoraScreen(onNavigateBack = {})
}
