package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    onNavigateBack: () -> Unit
) {
    val estudiantes = MockData.estudiantesSeguimiento
    val participaron = estudiantes.count { it.participo }

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
            Text("Curso: 6.º básico A", fontWeight = FontWeight.Bold)
            Text("Estudiantes con participación: $participaron de ${estudiantes.size}")

            Text("Lista de Alumnos:", fontWeight = FontWeight.Bold)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estudiantes) { est ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(est.nombre, fontWeight = FontWeight.Bold)
                            Text(if (est.participo) "Participó (${est.registros})" else "Sin registro")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeguimientoScreenPreview() {
    SeguimientoScreen(onNavigateBack = {})
}
