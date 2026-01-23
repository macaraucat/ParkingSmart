package com.example.parkingsmart.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable que muestra la pantalla de confirmación y resumen del pago.
 *
 * Esta pantalla presenta un resumen del tiempo total y el monto a pagar.
 * Un botón permite al usuario confirmar el pago y salir de la aplicación.
 *
 * @param montoRecibido El monto total del pago a mostrar.
 * @param tiempoRecibido El tiempo total de estacionamiento a mostrar.
 * @param onPagoFinalizado Una función de devolución de llamada que se invoca cuando el usuario
 * presiona el botón para finalizar el pago y salir.
 */
@Composable
fun PagoScreen(
    montoRecibido: Double,
    tiempoRecibido: String,
    onPagoFinalizado: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50), // Un color verde para indicar éxito.
            modifier = Modifier.size(80.dp)
        )

        Text(
            text = "Resumen de Pago",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Tarjeta que contiene los detalles del pago.
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(30.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                DetalleFila("Tiempo Total", tiempoRecibido)
                HorizontalDivider() // Separador visual.
                DetalleFila("Total a Pagar", "$${montoRecibido.toInt()}", true)
            }
        }

        // Botón para confirmar el pago y finalizar el proceso.
        Button(
            onClick = onPagoFinalizado,
            modifier = Modifier.padding(top = 30.dp).fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF105DFB),
                contentColor = Color.White
            )
        ) {
            Text("Confirmar Pago y Salir", style = MaterialTheme.typography.labelLarge)
        }
    }
}

/**
 * Composable para mostrar una fila de detalle con un título y un valor.
 *
 * Se utiliza para construir el resumen del pago, mostrando una etiqueta y su valor correspondiente.
 * Permite un estilo destacado para la información importante.
 *
 * @param titulo El texto que se mostrará como etiqueta o título de la fila.
 * @param valor El texto que se mostrará como el valor asociado al título.
 * @param esDestacado Un booleano que indica si la fila debe tener un estilo visual destacado (texto más grande y color diferente).
 */
@Composable
fun DetalleFila(titulo: String, valor: String, esDestacado: Boolean = false) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text(titulo, color = Color.Gray, fontSize = 16.sp)
        Text(
            text = valor,
            fontSize = if (esDestacado) 24.sp else 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (esDestacado) Color(0xFF2E64FE) else Color.Black
        )
    }
}
