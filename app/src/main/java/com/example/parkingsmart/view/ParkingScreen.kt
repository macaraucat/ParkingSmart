package com.example.parkingsmart.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parkingsmart.ui.theme.Blue
import com.example.parkingsmart.ui.theme.Gray
import com.example.parkingsmart.ui.theme.White
import com.example.parkingsmart.viewmodel.ParkingViewModel

@Composable
fun ParkingScreen(
    viewModel: ParkingViewModel = viewModel(),
    onNavegarAlPago: (Double, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.iniciarCronometro()
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
                    Text("#${state.numeroEspacio}", style = MaterialTheme.typography.displayMedium)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tiempo estacionado", color = Gray)
                    Text(state.tiempoFormateado, style = MaterialTheme.typography.displayLarge)
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Costo actual", fontSize = 14.sp, color = Gray)
                        Text("$${state.costoActual.toInt()}", style = MaterialTheme.typography.titleMedium)
                        Text("$${state.tarifaPorMinuto.toInt()}/minuto", style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }

        Button(
            onClick = {
                onNavegarAlPago(state.costoActual, state.tiempoFormateado)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Generar Boleta de Pago", style = MaterialTheme.typography.labelLarge)
        }
    }
}