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
import com.example.parkingsmart.view.LoginScreen
import com.example.parkingsmart.view.RegistroScreen
import com.example.parkingsmart.view.DashboardScreen
import com.example.parkingsmart.view.ParkingScreen
import com.example.parkingsmart.view.ParkingMainScreen
import com.example.parkingsmart.view.MetodoPagoScreen
import com.example.parkingsmart.view.PagoScreen
import com.example.parkingsmart.viewmodel.UsuarioViewModel
import com.example.parkingsmart.viewmodel.ParkingViewModel
import com.example.parkingsmart.viewmodel.PagoViewModel

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
                NavHost(navController = navController, startDestination = "login") {

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
                            onEspacioSeleccionado = {
                                navController.navigate("parking_camera")
                            }
                        )
                    }

                    composable("parking_camera") {
                        ParkingMainScreen(navController = navController)
                    }

                    composable("parking_screen") {
                        ParkingScreen(
                            viewModel = parkingViewModel,
                            onNavegarAlPago = { monto, tiempo ->
                                navController.navigate("metodopago/$monto/$tiempo")
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
