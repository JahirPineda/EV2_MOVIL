package cl.duoc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    rol: String,
    onNavigateBack: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val esDocente = rol == "docente"
    val nombre = if (esDocente) "María González" else "Matías Rojas"
    val tipo = if (esDocente) "Docente de Asignatura" else "Estudiante - 6.º básico A"
    val correo = if (esDocente) "maria.gonzalez@escuelasanbernardo.cl" else "matias.rojas@estudiantes.cl"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Rol: $tipo")
                    Text("Establecimiento: Escuela Básica de San Bernardo")
                    Text("Correo: $correo")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Acerca de la Aplicación", fontWeight = FontWeight.Bold)
                    Text("Bitácora Escolar — Proyecto de la asignatura DSY1105 (Desarrollo de Aplicaciones Móviles) en Duoc UC.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión")
            }

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    PerfilScreen(
        rol = "docente",
        onNavigateBack = {},
        onCerrarSesion = {}
    )
}
