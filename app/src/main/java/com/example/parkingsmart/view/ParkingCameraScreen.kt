package com.example.parkingsmart.view


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkingsmart.R
import com.example.parkingsmart.viewmodel.ParkingViewModel

/**
 * Composable que representa la pantalla principal de estacionamiento, que incluye una opción de cámara.
 *
 * Esta pantalla ofrece dos funciones principales:
 * 1. Confirmar el estacionamiento, lo que inicia un cronómetro y navega a la pantalla de seguimiento del estacionamiento.
 * 2. Abrir la cámara para reportar una anomalía si el espacio seleccionado está ocupado.
 *
 * Gestiona la solicitud de permisos de la cámara y alterna entre la vista de información y la vista de la cámara.
 *
 * @param navController El [NavController] para la navegación entre pantallas.
 * @param viewModel El [ParkingViewModel] que gestiona el estado y la lógica del estacionamiento.
 */
@Composable
fun ParkingMainScreen(
    navController: NavController,
    viewModel: ParkingViewModel = viewModel()
) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    // Lanzador para solicitar el permiso de la cámara.
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasCameraPermission = isGranted }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Si `showCamera` es falso, muestra la pantalla de información.
        if (!showCamera) {
            Image(
                painter = painterResource(id = R.drawable.car),
                contentDescription = "Auto Estacionado",
                modifier = Modifier.size(400.dp))

            // Botón para confirmar el estacionamiento e iniciar el cronómetro.
            Button(
                onClick = { 
                    viewModel.iniciarCronometro()
                    navController.navigate("parking")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)
            ){
                Text("Confirmar estacionamiento", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para abrir la cámara.
            Button(
                onClick = {
                    if (hasCameraPermission) {
                        showCamera = true
                    } else {
                        launcher.launch(android.Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
            ) {
                Text("Abrir cámara", style = MaterialTheme.typography.labelLarge)
            }

            // Tarjeta con texto informativo.
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAEAEA)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            ) {
                Text(
                    text = "Si se presenta algún problema o anomalía con el estacionamiento seleccionado (ej. aparece disponible pero está ocupado), puedes capturar una foto y enviarla como reporte.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Si `showCamera` es verdadero, muestra la vista de la cámara.
            Box(modifier = Modifier.fillMaxSize()) {
                CameraScreen()
                // Botón para volver a la pantalla de información.
                IconButton(
                    onClick = { showCamera = false },
                    modifier = Modifier.padding(top = 10.dp).align(Alignment.TopStart)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Volver", 
                        tint = Color.White, 
                        modifier = Modifier.size(50.dp))
                }
            }
        }
    }
}
