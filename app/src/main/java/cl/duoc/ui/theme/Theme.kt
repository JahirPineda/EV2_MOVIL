package cl.duoc.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Paleta de colores para Modo Oscuro.
 */
private val DarkColorScheme = darkColorScheme(
    primary = AzulPrincipal,
    onPrimary = SuperficieBlanco,
    primaryContainer = AzulOscuro,
    onPrimaryContainer = AzulClaro,
    secondary = AzulClaro,
    onSecondary = AzulOscuro,
    background = TextoPrincipal,
    surface = Color(0xFF1E293B),
    onSurface = SuperficieBlanco,
    onBackground = SuperficieBlanco
)

/**
 * Paleta de colores para Modo Claro.
 */
private val LightColorScheme = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = SuperficieBlanco,
    primaryContainer = AzulContainer,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulOscuro,
    onSecondary = SuperficieBlanco,
    background = FondoClaro,
    surface = SuperficieBlanco,
    onBackground = TextoPrincipal,
    onSurface = TextoPrincipal,
    outline = BordeSuave,
    surfaceVariant = FondoClaro,
    onSurfaceVariant = TextoSecundario
)

/**
 * Tema principal de la aplicación basado en Material Design 3.
 */
@Composable
fun BitacoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}