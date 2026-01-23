package com.example.parkingsmart.view

import CameraScreen
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

@Composable
fun ParkingMainScreen(
    navController: NavController,
    viewModel: ParkingViewModel = viewModel()
) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasCameraPermission = isGranted }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!showCamera) {
            Image(
                painter = painterResource(id = R.drawable.car),
                contentDescription = "Auto Estacionado",
                modifier = Modifier.size(400.dp))

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
            Box(modifier = Modifier.fillMaxSize()) {
                CameraScreen()
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
