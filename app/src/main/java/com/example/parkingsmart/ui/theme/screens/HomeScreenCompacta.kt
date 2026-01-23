package com.example.parkingsmart.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.parkingsmart.ui.theme.utils.obtenerWindowSizeClass

/**
 * Este archivo contiene la implementación de la pantalla de inicio para anchos de ventana compactos.
 */

/**
 * Composable que define la interfaz de usuario para la pantalla de inicio en un tamaño de ventana compacto.
 *
 * Utiliza un [Scaffold] con una [TopAppBar] y un [Column] para organizar el contenido.
 * Muestra un mensaje de bienvenida y un botón de ejemplo.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Texto de prueba") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "¡Bienvenido!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = { }) {
                Text("Presióname")
            }
        }
    }
}

/**
 * Composable que actúa como un enrutador para la pantalla de inicio, seleccionando el diseño
 * apropiado según la clase de tamaño de la ventana.
 *
 * Actualmente, solo muestra [HomeScreenCompacta] para la clase de ancho de ventana [WindowWidthSizeClass.Compact].
 */
@Composable
fun HomeScreen(){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
    }
}

/**
 * Vista previa para la pantalla de inicio en un diseño compacto.
 *
 * Esta función de previsualización muestra cómo se verá [HomeScreenCompacta] en un dispositivo
 * con un ancho de 480 dp y una altura de 600 dp.
 */
@Preview("Compact", widthDp = 480, heightDp = 600)
@Composable
fun PreviewCompact(){
    HomeScreenCompacta()
}
