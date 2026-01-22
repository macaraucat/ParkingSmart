package com.example.parkingsmart.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkingsmart.R
import com.example.parkingsmart.ui.theme.Blue
import com.example.parkingsmart.ui.theme.Gray
import com.example.parkingsmart.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineGranted || coarseGranted) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            Toast.makeText(context, "Debe aceptar los permisos de ubicación para continuar", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(180.dp).padding(bottom = 16.dp)
        )

        Text("ParkingSmart", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Sistema de estacionamiento inteligente",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = estado.patente,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = viewModel::onPatenteChange,
                label = { Text("Patente", color = Color.Black) },
                textStyle = TextStyle(color = Color.Black),
                isError = estado.errores.patente != null,
                supportingText = {
                    estado.errores.patente?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = estado.clave,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = viewModel::onClaveChange,
                label = { Text("Clave", color = Color.Black) },
                textStyle = TextStyle(color = Color.Black),
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            Button(
                onClick = {
                    if (viewModel.validarLogIn()) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(68.dp).padding(top = 14.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Iniciar Sesión", style = MaterialTheme.typography.labelLarge)
            }

            Button(
                onClick = { navController.navigate("registro") },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
