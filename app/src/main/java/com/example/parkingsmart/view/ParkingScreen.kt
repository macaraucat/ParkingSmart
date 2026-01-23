package com.example.parkingsmart.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingsmart.ui.theme.Blue
import com.example.parkingsmart.ui.theme.Gray
import com.example.parkingsmart.ui.theme.White
import com.example.parkingsmart.viewmodel.ParkingViewModel
import com.example.parkingsmart.viewmodel.UsuarioViewModel

@Composable
fun ParkingScreen(
    parkingViewModel: ParkingViewModel, // Quitamos el = viewModel()
    usuarioViewModel: UsuarioViewModel, // Quitamos el = viewModel()
    onNavegarAlPago: (Double, String, String) -> Unit
) {
    val parkingUiState by parkingViewModel.uiState.collectAsState()
    val usuarioUiState by usuarioViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        parkingViewModel.iniciarCronometro()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        Text("Estacionamiento Activo", style = MaterialTheme.typography.headlineMedium)

        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Espacio", color = Gray)
                    Text("#${parkingUiState.numeroEspacio}", style = MaterialTheme.typography.displayMedium)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tiempo estacionado", color = Gray)
                    Text(parkingUiState.tiempoFormateado, style = MaterialTheme.typography.displayLarge)
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Costo actual", fontSize = 14.sp, color = Gray)
                        Text("$${parkingUiState.costoActual.toInt()}", style = MaterialTheme.typography.titleMedium)
                        Text("$${parkingUiState.tarifaPorMinuto.toInt()}/minuto", style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }

        Button(
            onClick = {
                onNavegarAlPago(parkingUiState.costoActual, parkingUiState.tiempoFormateado, usuarioUiState.correo)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Generar Boleta de Pago", style = MaterialTheme.typography.labelLarge)
        }
    }
}