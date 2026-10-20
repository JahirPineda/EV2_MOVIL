package cl.duoc.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.ui.theme.GrisBorrador
import cl.duoc.ui.theme.RojoError
import cl.duoc.ui.theme.VerdeExito
import cl.duoc.viewmodel.BitacorasListViewModel

/**
 * Pantalla que muestra el listado completo de bitácoras almacenadas en Room.
 * Permite buscar por texto y filtrar por estado o fecha.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaBitacorasScreen(
    rol: String,
    viewModel: BitacorasListViewModel,
    onNavigateBack: () -> Unit,
    onVerDetalleBitacora: (String) -> Unit
) {
    // Obtenemos la lista de bitácoras desde Room
    val listaBitacoras by viewModel.bitacoras.collectAsState()

    // Estados para búsqueda y filtros
    var textoBusqueda by remember { mutableStateOf("") }
    var filtroEstado by remember { mutableStateOf("Todas") }
    var filtroFecha by remember { mutableStateOf("Todas") }
    var mostrarFiltros by remember { mutableStateOf(false) }

    // Filtramos la lista según los criterios seleccionados
    val bitacorasFiltradas = listaBitacoras.filter { bitacora ->
        val coincideTexto = bitacora.titulo.contains(textoBusqueda, ignoreCase = true) ||
                bitacora.descripcion.contains(textoBusqueda, ignoreCase = true)

        val coincideEstado = if (filtroEstado == "Todas") true else bitacora.estado.equals(filtroEstado, ignoreCase = true)
        val coincideFecha = if (filtroFecha == "Todas") true else bitacora.fecha.contains(filtroFecha, ignoreCase = true)

        coincideTexto && coincideEstado && coincideFecha
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Bitácoras") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón para mostrar u ocultar la barra de búsqueda y filtros
                    IconButton(onClick = { mostrarFiltros = !mostrarFiltros }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros y Búsqueda")
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
            // Panel desplegable de búsqueda y filtros
            AnimatedVisibility(
                visible = mostrarFiltros,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        label = { Text("Buscar por título o descripción") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text("Filtrar por Estado:", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Todas", "PUBLICADA", "BORRADOR", "CERRADA").forEach { estado ->
                            FilterChip(
                                selected = filtroEstado.equals(estado, ignoreCase = true),
                                onClick = { filtroEstado = estado },
                                label = { Text(estado) }
                            )
                        }
                    }

                    Text("Filtrar por Fecha / Mes:", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Todas", "10/2026", "2026").forEach { fecha ->
                            FilterChip(
                                selected = filtroFecha.equals(fecha, ignoreCase = true),
                                onClick = { filtroFecha = fecha },
                                label = { Text(if (fecha == "Todas") "Todas las fechas" else fecha) }
                            )
                        }
                    }
                }
            }

            // Indicador de cuántos elementos hay
            Text(
                text = "Mostrando ${bitacorasFiltradas.size} bitácoras",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            // Lista con desplazamiento vertical (LazyColumn)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bitacorasFiltradas) { bitacora ->
                    // Color del punto según el estado
                    val colorEstado = when (bitacora.estado.uppercase()) {
                        "PUBLICADA" -> VerdeExito
                        "BORRADOR" -> GrisBorrador
                        "CERRADA" -> RojoError
                        else -> GrisBorrador
                    }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = bitacora.titulo,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                // Indicador visual del estado
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(colorEstado, CircleShape)
                                    )
                                    Text(bitacora.estado, style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            Text("Fecha: ${bitacora.fecha}")
                            Text("Descripción: ${bitacora.descripcion.take(60)}...")

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { onVerDetalleBitacora(bitacora.id.toString()) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver Detalle")
                            }
                        }
                    }
                }
            }
        }
    }
}
