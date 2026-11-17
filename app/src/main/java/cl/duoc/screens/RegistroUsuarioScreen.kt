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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.viewmodel.RegisterViewModel

/**
 * Pantalla para registrar un nuevo usuario (Docente o Estudiante) en la base de datos Room.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroUsuarioScreen(
    viewModel: RegisterViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Muestra avisos emergentes (Snackbar) si hay un error o confirmación de éxito
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let { snackbarHostState.showSnackbar(it) }
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Nuevo Usuario") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Crear Cuenta",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            // Selector de Rol: ESTUDIANTE o DOCENTE
            Text("Seleccione su Rol:", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ESTUDIANTE", "DOCENTE").forEach { rolOpcion ->
                    FilterChip(
                        selected = uiState.rol == rolOpcion,
                        onClick = { viewModel.onRolChanged(rolOpcion) },
                        label = { Text(rolOpcion) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // RUT
            OutlinedTextField(
                value = uiState.rut,
                onValueChange = { viewModel.onRutChanged(it) },
                label = { Text("RUT (ej. 12.345.678-9) *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Nombre completo
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreChanged(it) },
                label = { Text("Nombre Completo *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Correo electrónico
            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { viewModel.onCorreoChanged(it) },
                label = { Text("Correo Electrónico *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Contraseña
            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = { viewModel.onContrasenaChanged(it) },
                label = { Text("Contraseña *") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Curso
            OutlinedTextField(
                value = uiState.cursoAsignado,
                onValueChange = { viewModel.onCursoChanged(it) },
                label = { Text("Curso (ej. 6.º básico A)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Asignatura (solo si el rol es DOCENTE)
            if (uiState.rol == "DOCENTE") {
                OutlinedTextField(
                    value = uiState.asignaturaAsignada,
                    onValueChange = { viewModel.onAsignaturaChanged(it) },
                    label = { Text("Asignatura Asignada (Docente)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón de Registrarse
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        viewModel.registrarUsuario {
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarse")
                }
            }
        }
    }
}
