package com.example.parkingsmart.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import com.example.parkingsmart.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parkingsmart.viewmodel.UsuarioViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.parkingsmart.ui.theme.Blue

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

        if (granted) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            Toast.makeText(context, "Se requieren permisos de ubicación para usar la app", Toast.LENGTH_LONG).show()

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp).padding(bottom = 20.dp)
        )

        Text("Formulario de Registro",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = estado.errores.nombre != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = { estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            OutlinedTextField(
                value = estado.patente,
                onValueChange = viewModel::onPatenteChange,
                label = { Text("Patente") },
                isError = estado.errores.patente != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = { estado.errores.patente?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo") },
                isError = estado.errores.correo != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = { estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Clave") },
                isError = estado.errores.clave != null,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = { estado.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange
                )
                Text("Acepto los términos y condiciones", Modifier.padding(end = 20.dp))
            }

            Button(
                onClick = {
                    if (!estado.aceptaTerminos) {
                        Toast.makeText(context, "Debe aceptar los términos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.registrarUsuario(
                        onSuccess = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}