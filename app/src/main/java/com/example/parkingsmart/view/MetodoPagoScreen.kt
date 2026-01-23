package com.example.parkingsmart.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.parkingsmart.viewmodel.PagoViewModel

/**
 * Composable que representa la pantalla para seleccionar un método de pago.
 *
 * Esta pantalla permite al usuario elegir entre diferentes métodos de pago (Efectivo o Tarjeta).
 * Muestra el monto total a pagar y un indicador de progreso mientras se procesa el pago.
 * Una vez que el pago es exitoso, navega a la siguiente pantalla.
 *
 * @param viewModel El [PagoViewModel] que gestiona el estado y la lógica del pago.
 * @param monto El monto total a pagar.
 * @param tiempo El tiempo total de estacionamiento.
 * @param correoUsuario El correo electrónico del usuario para asociar el pago.
 * @param onPagoExitoso Una función de devolución de llamada que se invoca cuando el pago se ha completado con éxito.
 */
@Composable
fun MetodoPagoScreen(
    viewModel: PagoViewModel,
    monto: Double,
    tiempo: String,
    correoUsuario: String,
    onPagoExitoso: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Inicializa el estado del pago con el monto y tiempo recibidos.
    LaunchedEffect(Unit) {
        viewModel.inicializarPago(monto, tiempo)
    }

    // Observa el estado de pago exitoso para navegar a la siguiente pantalla.
    LaunchedEffect(state.pagoExitoso) {
        if (state.pagoExitoso) onPagoExitoso()
    }

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
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        // Muestra un indicador de progreso si el pago se está procesando.
        if (state.estaProcesando) {
            CircularProgressIndicator(color = Color.Blue) // Puedes personalizar el color.
            Text("Procesando pago...", modifier = Modifier.padding(top = 8.dp))
        } else {
            // Botones para seleccionar el método de pago.
            BotonMetodoPago("Efectivo") {
                viewModel.seleccionarMetodoPago("Efectivo")
                viewModel.procesarPago(correoUsuario)
            }

            BotonMetodoPago("Tarjeta de Crédito/Débito") {
                viewModel.seleccionarMetodoPago("Tarjeta")
                viewModel.procesarPago(correoUsuario)
            }
        }

        // Muestra un mensaje de error si ocurre alguno.
        state.mensajeError?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

/**
 * Un Composable de botón estilizado para las opciones de método de pago.
 *
 * @param texto El texto que se mostrará en el botón.
 * @param onClick La acción que se ejecutará cuando se haga clic en el botón.
 */
@Composable
fun BotonMetodoPago(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(72.dp).padding(top = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B607))
    ) {
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
}
