package cl.duoc.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.ui.components.LogoColegio
import cl.duoc.viewmodel.LoginViewModel

/**
 * Pantalla de inicio de sesión.
 * Permite a docentes y estudiantes autenticarse mediante su RUT y contraseña.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Obtenemos el estado actual del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Animación suave de aparición al abrir la pantalla
    val visibilidad = remember { Animatable(0f) }
    val desplazamientoY = remember { Animatable(40f) }

    LaunchedEffect(Unit) {
        // Animamos la opacidad y la posición en 800 milisegundos
        visibilidad.animateTo(1f, animationSpec = tween(800))
        desplazamientoY.animateTo(0f, animationSpec = tween(800))
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .alpha(visibilidad.value)
                .offset(y = desplazamientoY.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo oficial del colegio
            LogoColegio(
                size = 110.dp,
                mostrarTexto = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Título principal
            Text(
                text = "Bitácora Escolar",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Escuela Básica de San Bernardo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo para el RUT
            OutlinedTextField(
                value = uiState.rut,
                onValueChange = { viewModel.onRutChanged(it) },
                label = { Text("RUT (ej. 11.111.111-1)") },
                isError = uiState.rutError != null,
                trailingIcon = {
                    if (uiState.rutError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (uiState.rutError != null) {
                        Text(uiState.rutError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo para la Contraseña
            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = { viewModel.onContrasenaChanged(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.contrasenaError != null,
                trailingIcon = {
                    if (uiState.contrasenaError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (uiState.contrasenaError != null) {
                        Text(uiState.contrasenaError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checkbox para Recordar Usuario en SharedPreferences
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = uiState.recordarUsuario,
                    onCheckedChange = { viewModel.onRecordarUsuarioChanged(it) }
                )
                Text(
                    text = "Recordar usuario",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Mensaje de error general si las credenciales fallan
            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Inicio de Sesión o Indicador de carga
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        viewModel.login { rol ->
                            onLoginSuccess(rol)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Botón para ir al registro
                OutlinedButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿No tienes cuenta? Regístrate aquí")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Proyecto DSY1105 - Duoc UC",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
