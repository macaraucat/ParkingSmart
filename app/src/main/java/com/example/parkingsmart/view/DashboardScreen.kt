package com.example.parkingsmart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.parkingsmart.viewmodel.PagoViewModel
import com.example.parkingsmart.viewmodel.ParkingViewModel
import com.example.parkingsmart.viewmodel.UsuarioViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun DashboardScreen(
    navController: NavHostController,
    parkingViewModel: ParkingViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    pagoViewModel: PagoViewModel = viewModel(),
    onEspacioSeleccionado: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 80.dp),
        ) {

            Text("Estacionamientos", style = MaterialTheme.typography.headlineMedium)

            Text("Seleccione un espacio disponible",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            val estacionamientos = (1..20).toList()
            val columnas = 5
            val chunkedEstacionamientos = estacionamientos.chunked(columnas)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                chunkedEstacionamientos.forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { numero ->
                            val esOcupado = (numero % 7 == 0)
                            val colorBoton = if (esOcupado) Color(0xFFF00000) else Color(0xFF00B607)

                            Button(
                                onClick = {
                                    if (!esOcupado) {
                                        parkingViewModel.seleccionarYActivarEspacio(numero)
                                        onEspacioSeleccionado()
                                    }
                                },
                                modifier = Modifier.size(58.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                                enabled = !esOcupado,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(numero.toString(),style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                LeyendaItem(Color(0xFF00B607), "Disponible     ")
                LeyendaItem(Color(0xFFF00000), "Ocupado")
            }

            Text("Tu Ubicación actual", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 30.dp, bottom = 10.dp))

            val defaultPos = LatLng(-33.4489, -70.6693)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(defaultPos, 15f)
            }
            val markerState = rememberMarkerState(position = defaultPos)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(horizontal = 30.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = true
                    )
                ) {
                    Marker(
                        state = markerState,
                        title = "Usted está aquí",
                        snippet = "Estacionamientos cerca de su zona"
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 26.dp, end = 12.dp)
                .height(38.dp)
                .width(114.dp)
                .zIndex(1f),
            onClick = {
                usuarioViewModel.limpiarDatos()
                pagoViewModel.reiniciarEstado()
                parkingViewModel.cerrarSesion()
                
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAF0000)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Log Out", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun LeyendaItem(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, shape = RoundedCornerShape(4.dp))
        )
        Text(
            text = texto,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}