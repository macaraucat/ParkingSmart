package com.example.parkingsmart.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.parkingsmart.viewmodel.ParkingViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
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
    onEspacioSeleccionado: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val uiState by parkingViewModel.uiState.collectAsState()

    // Efecto para obtener la ubicación al entrar
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val nuevaPos = LatLng(it.latitude, it.longitude)
                    parkingViewModel.actualizarUbicacion(nuevaPos)
                }
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uiState.ubicacionActual, 15f)
    }

    // Sincronizamos la cámara si la ubicación cambia en el ViewModel
    LaunchedEffect(uiState.ubicacionActual) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(uiState.ubicacionActual, 15f)
        )
    }

    val markerState = rememberMarkerState(position = uiState.ubicacionActual)
    
    // Actualizamos el marcador si la ubicación cambia
    LaunchedEffect(uiState.ubicacionActual) {
        markerState.position = uiState.ubicacionActual
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp),
        ) {
            Text("Tu ubicación actual", style = MaterialTheme.typography.headlineMedium)

            Box(modifier = Modifier.fillMaxWidth().height(240.dp).padding(horizontal = 30.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize().padding(top = 10.dp),
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

            Text("Estacionamientos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 30.dp))

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
                                colors = ButtonDefaults.buttonColors(containerColor = colorBoton, contentColor = Color.White),
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
                LeyendaItem(Color(0xFF00B607), "Disponible")
                LeyendaItem(Color(0xFFE0E0E0), "Procesando")
                LeyendaItem(Color(0xFFF00000), "Ocupado")
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 26.dp, end = 4.dp)
                .height(38.dp)
                .width(180.dp)
                .zIndex(1f),
            onClick = {
                navController.navigate("login") {
                    popUpTo(0)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
        ) {
            Text("CERRAR SESIÓN", style = MaterialTheme.typography.labelMedium)
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
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}