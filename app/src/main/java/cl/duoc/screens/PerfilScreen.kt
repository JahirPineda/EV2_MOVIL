package cl.duoc.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.data.SessionManager
import cl.duoc.viewmodel.PerfilViewModel
import coil.compose.rememberAsyncImagePainter

/**
 * Pantalla de perfil de usuario. Muestra los datos personales del docente o estudiante,
 * permite cambiar su foto de perfil desde la galería y modificar sus datos personales.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    rol: String,
    viewModel: PerfilViewModel? = null,
    onNavigateBack: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuarioActual by SessionManager.usuarioActual.collectAsState()
    val uiState = viewModel?.uiState?.collectAsState()?.value

    val esDocente = rol.equals("docente", ignoreCase = true) || usuarioActual?.rol == "DOCENTE"

    val nombre = usuarioActual?.nombre ?: if (esDocente) "María González" else "Matías Rojas"
    val tipoRol = if (esDocente) "DOCENTE" else "ESTUDIANTE"
    val correo = usuarioActual?.correo ?: if (esDocente) "profesora.maria@colegio.cl" else "matias.rojas@estudiante.cl"
    val rut = usuarioActual?.rut ?: usuarioActual?.id ?: "11.111.111-1"
    val curso = usuarioActual?.cursoAsignado ?: "6.º básico A"
    val asignatura = usuarioActual?.asignaturaAsignada ?: "Matemáticas"

    // Estado local para la foto de perfil elegida
    var fotoPerfilUri by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Lanzador nativo para elegir una imagen desde la galería
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            fotoPerfilUri = uri.toString()
        }
    }

    val isEditing = uiState?.isEditing ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Perfil" else "Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditing) {
                            viewModel?.cancelarEdicion()
                        } else {
                            onNavigateBack()
                        }
                    }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Avatar circular de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (fotoPerfilUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = fotoPerfilUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar por defecto",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Botón para abrir la galería y cambiar avatar
            OutlinedButton(
                onClick = {
                    launcherGaleria.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Text("Cambiar Foto de Perfil (Galería)")
            }

            // Mensajes de éxito o error
            uiState?.successMessage?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            uiState?.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (isEditing && uiState != null) {
                // FORMULARIO DE EDICIÓN DE PERFIL
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Modificar Información",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // RUT (Identificador no editable)
                        OutlinedTextField(
                            value = rut,
                            onValueChange = {},
                            label = { Text("RUT (Identificador no modificable)") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Nombre completo
                        OutlinedTextField(
                            value = uiState.nombre,
                            onValueChange = { viewModel.onNombreChanged(it) },
                            label = { Text("Nombre Completo") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Correo electrónico
                        OutlinedTextField(
                            value = uiState.correo,
                            onValueChange = { viewModel.onCorreoChanged(it) },
                            label = { Text("Correo Electrónico") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Contraseña
                        OutlinedTextField(
                            value = uiState.contrasena,
                            onValueChange = { viewModel.onContrasenaChanged(it) },
                            label = { Text("Contraseña") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Curso asignado
                        OutlinedTextField(
                            value = uiState.cursoAsignado,
                            onValueChange = { viewModel.onCursoChanged(it) },
                            label = { Text("Curso Asignado") },
                            placeholder = { Text("Ej: 6.º básico A") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Asignatura asignada (Solo docentes)
                        if (esDocente) {
                            OutlinedTextField(
                                value = uiState.asignaturaAsignada,
                                onValueChange = { viewModel.onAsignaturaChanged(it) },
                                label = { Text("Asignatura Asignada") },
                                placeholder = { Text("Ej: Matemáticas") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Botones de guardar y cancelar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.cancelarEdicion() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }

                            Button(
                                onClick = { viewModel.guardarCambios(usuarioActual) },
                                enabled = !uiState.isLoading,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Guardar")
                                }
                            }
                        }
                    }
                }
            } else {
                // TARJETA DE VISUALIZACIÓN DE INFORMACIÓN PERSONAL
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            if (viewModel != null && usuarioActual != null) {
                                IconButton(onClick = { viewModel.iniciarEdicion(usuarioActual) }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar Perfil",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Text("Rol: $tipoRol")
                        Text("RUT: $rut")
                        Text("Establecimiento: Escuela Básica de San Bernardo")
                        Text("Correo: $correo")
                        Text("Curso Asignado: $curso")
                        if (esDocente) {
                            Text("Asignatura Asignada: $asignatura")
                        }
                    }
                }

                // Botón destacado para modificar perfil
                if (viewModel != null && usuarioActual != null) {
                    Button(
                        onClick = { viewModel.iniciarEdicion(usuarioActual) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Modificar Información de Perfil")
                    }
                }

                // Tarjeta informativa institucional
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Acerca de la Aplicación", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
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
