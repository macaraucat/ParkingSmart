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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkingsmart.R
import com.example.parkingsmart.viewmodel.UsuarioViewModel

/**
 * Composable que representa la pantalla de inicio de sesión de la aplicación.
 *
 * Esta pantalla permite a los usuarios iniciar sesión con su correo electrónico y contraseña.
 * También proporciona una opción para navegar a la pantalla de registro. Una vez que el
 * inicio de sesión es exitoso, solicita permisos de ubicación antes de navegar al dashboard.
 *
 * @param navController El [NavController] utilizado para la navegación entre pantallas.
 * @param viewModel El [UsuarioViewModel] que gestiona el estado y la lógica de la interfaz de usuario.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Lanzador para solicitar permisos de ubicación.
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        // Si se otorgan los permisos, navega al dashboard.
        if (fineGranted || coarseGranted) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            // Muestra un mensaje si los permisos son denegados.
            Toast.makeText(context, "Se requieren permisos para continuar", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
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

            // Campo de texto para el correo electrónico.
            OutlinedTextField(
                value = uiState.correo,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo Electrónico", color = Color.Black) },
                isError = uiState.errores.correo != null,
                supportingText = {
                    uiState.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            // Campo de texto para la contraseña.
            OutlinedTextField(
                value = uiState.clave,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = viewModel::onClaveChange,
                label = { Text("Clave", color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.errores.clave != null,
                supportingText = {
                    uiState.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            // Botón para iniciar sesión.
            Button(
                onClick = {
                    viewModel.loginUsuario(
                        onSuccess = {
                            // Al iniciar sesión con éxito, solicita permisos.
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(top = 14.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Iniciar Sesión", style = MaterialTheme.typography.labelLarge)
            }

            // Botón para navegar a la pantalla de registro.
            Button(
                onClick = {
                    viewModel.limpiarDatos()
                    navController.navigate("registro")
                },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
