package cl.duoc.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.R

/**
 * Componente visual que representa el logo oficial de la "Escuela Básica de San Bernardo".
 */
@Composable
fun LogoColegio(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    mostrarTexto: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícono del logotipo con sombra y borde circular
        Box(
            modifier = Modifier
                .size(size)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_school_logo),
                contentDescription = "Logo Escuela Básica de San Bernardo",
                modifier = Modifier
                    .size(size)
                    .padding(4.dp)
            )
        }

        if (mostrarTexto) {
            Text(
                text = "Escuela Básica de San Bernardo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = "Excelencia y Formación Integral",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
