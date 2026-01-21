package com.example.parkingsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.parkingsmart.ui.theme.ParkingSmartTheme
import com.example.parkingsmart.view.DashboardScreen
import com.example.parkingsmart.view.LoginScreen
import com.example.parkingsmart.view.MetodoPagoScreen
import com.example.parkingsmart.view.PagoScreen
import com.example.parkingsmart.view.ParkingScreen
import com.example.parkingsmart.view.RegistroScreen
import com.example.parkingsmart.viewmodel.PagoViewModel
import com.example.parkingsmart.viewmodel.ParkingViewModel
import com.example.parkingsmart.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val usuarioViewModel: UsuarioViewModel = viewModel()
            val parkingViewModel: ParkingViewModel = viewModel()
            val pagoViewModel: PagoViewModel = viewModel()

            ParkingSmartTheme {
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            viewModel = usuarioViewModel
                        )
                    }

                    composable("registro") {
                        RegistroScreen(
                            navController = navController,
                            viewModel = usuarioViewModel
                        )
                    }

                    composable("dashboard") {
                        DashboardScreen(
                            navController = navController,
                            parkingViewModel = parkingViewModel,
                            usuarioViewModel = usuarioViewModel,
                            pagoViewModel = pagoViewModel,
                            onEspacioSeleccionado = {
                                navController.navigate("parking")
                            }
                        )
                    }

                    composable("parking") {
                        ParkingScreen(
                            viewModel = parkingViewModel,
                            onNavegarAlPago = { monto, tiempo ->
                                navController.navigate("metodopago/$monto/$tiempo")
                            }
                        )
                    }

                    composable(
                        route = "metodopago/{monto}/{tiempo}",
                        arguments = listOf(
                            navArgument("monto") { type = NavType.FloatType },
                            navArgument("tiempo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val monto = backStackEntry.arguments?.getFloat("monto")?.toDouble() ?: 0.0
                        val tiempo = backStackEntry.arguments?.getString("tiempo") ?: "00:00"

                        MetodoPagoScreen(
                            viewModel = pagoViewModel,
                            monto = monto,
                            tiempo = tiempo,
                            onPagoExitoso = {
                                navController.navigate("pago/$monto/$tiempo")
                            }
                        )
                    }

                    composable(
                        route = "pago/{monto}/{tiempo}",
                        arguments = listOf(
                            navArgument("monto") { type = NavType.FloatType },
                            navArgument("tiempo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->

                        val monto = backStackEntry.arguments?.getFloat("monto")?.toDouble() ?: 0.0
                        val tiempo = backStackEntry.arguments?.getString("tiempo") ?: "00:00"

                        PagoScreen(
                            montoRecibido = monto,
                            tiempoRecibido = tiempo,
                            onPagoFinalizado = {
                                pagoViewModel.reiniciarEstado()
                                // Al finalizar el pago también limpiamos datos para un nuevo ciclo
                                parkingViewModel.cerrarSesion()
                                usuarioViewModel.limpiarDatos()
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AppNavigationPreview() {
        val navController = rememberNavController()
        val usuarioViewModel: UsuarioViewModel = viewModel()

        ParkingSmartTheme {
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, usuarioViewModel) }
            }
        }
    }
}