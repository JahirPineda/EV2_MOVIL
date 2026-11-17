package cl.duoc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.ui.components.ClienteCard
import cl.duoc.viewmodel.SeguimientoViewModel

/**
 * Pantalla que permite al docente supervisar el avance del curso,
 * viendo qué alumnos han participado con aportes y quiénes faltan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    viewModel: SeguimientoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento del Curso") },
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
                text = "Curso: 6.º básico A",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            // Resumen numérico del curso
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Métricas Reales del Curso:", fontWeight = FontWeight.Bold)
                    Text("• Total Alumnos Matriculados: ${uiState.totalEstudiantes}")
                    Text("• Estudiantes Participantes: ${uiState.estudiantesParticipando} de ${uiState.totalEstudiantes}")
                }
            }

            Text("Lista Interactiva de Alumnos:", fontWeight = FontWeight.Bold)

            // Lista con el estado de cada estudiante
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.alumnos) { item ->
                    ClienteCard(
                        nombre = item.usuario.nombre,
                        correo = item.usuario.correo,
                        haParticipado = item.haParticipado,
                        cantidadRegistros = item.cantidadRegistros
                    )
                }
            }
        }
    }
}
