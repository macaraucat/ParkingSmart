package com.example.parkingsmart.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.parkingsmart.ui.theme.Gray
import com.example.parkingsmart.ui.theme.LightBlue
import com.example.parkingsmart.viewmodel.PagoViewModel

@Composable
fun MetodoPagoScreen(
    viewModel: PagoViewModel,
    monto: Double,
    tiempo: String,
    onPagoExitoso: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.inicializarPago(monto, tiempo) }
    LaunchedEffect(state.pagoExitoso) { if (state.pagoExitoso) onPagoExitoso() }

    Column(
        modifier = Modifier.fillMaxSize().padding(all = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Método de Pago", style = MaterialTheme.typography.headlineMedium)

        Text(text = "Seleccione un medio de pago", style = MaterialTheme.typography.headlineSmall)

        Text(
            text = "Total a pagar: $${state.montoTotal.toInt()}",
            style = MaterialTheme.typography.headlineSmall,
            color = Gray,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        if (state.estaProcesando) {
            CircularProgressIndicator(color = LightBlue)
            Text("Procesando pago...")
        } else {
            BotonMetodoPago("Efectivo") {
                viewModel.seleccionarMetodoPago("Efectivo")
                viewModel.procesarPago()
            }
            BotonMetodoPago("Tarjeta de Crédito/Débito") {
                viewModel.seleccionarMetodoPago("Tarjeta")
                viewModel.procesarPago()
            }
        }
        state.mensajeError?.let { Text(it, color = Color.Red) }
    }
}

@Composable
fun BotonMetodoPago(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B607))
    ) {
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
}