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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.data.MockData

/**
 * Pantalla de referencia para registrar aprendizaje (versión inicial con MockData).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    bitacoraId: String,
    onNavigateBack: () -> Unit
) {
    val bitacora = MockData.bitacoras.find { it.id == bitacoraId } ?: MockData.bitacoras.first()

    var fecha by remember { mutableStateOf("16/10/2026") }
    var contenidos by remember { mutableStateOf("") }
    var actividades by remember { mutableStateOf("") }
    var aprendizajes by remember { mutableStateOf("") }
    var dificultades by remember { mutableStateOf("") }

    val evidencias = remember { mutableStateListOf<String>() }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var mensajeDialogo by remember { mutableStateOf("") }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                onNavigateBack()
            },
            title = { Text("Registro de Aprendizaje") },
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
                title = { Text("Registrar Aprendizaje") },
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
            Text(
                text = "${bitacora.titulo} (${bitacora.asignatura})",
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de la clase") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contenidos,
                onValueChange = { contenidos = it },
                label = { Text("1. ¿Qué contenidos trabajaron hoy?") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = actividades,
                onValueChange = { actividades = it },
                label = { Text("2. ¿Qué actividad realizaron?") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = aprendizajes,
                onValueChange = { aprendizajes = it },
                label = { Text("3. ¿Qué aprendiste en esta clase?") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dificultades,
                onValueChange = { dificultades = it },
                label = { Text("4. ¿Qué dudas o dificultades tuviste?") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Archivos o Evidencias:", fontWeight = FontWeight.Bold)

            evidencias.forEach { item ->
                Text("• $item")
            }

            OutlinedButton(
                onClick = {
                    evidencias.add("foto_cuaderno_ejercicios.jpg")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adjuntar Foto o Guía")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    mensajeDialogo = "¡Tu registro de aprendizaje fue enviado a la profesora!"
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Registro")
            }

            OutlinedButton(
                onClick = {
                    mensajeDialogo = "Registro guardado como borrador."
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar como Borrador")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(bitacoraId = "1", onNavigateBack = {})
}
