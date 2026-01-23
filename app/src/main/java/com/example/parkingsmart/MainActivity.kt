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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkingsmart.repository.ParkingDatabase

/**
 * Actividad principal de la aplicación.
 *
 * Esta actividad es el punto de entrada de la aplicación y se encarga de configurar la navegación
 * y la inyección de dependencias para los ViewModels.
 */
class MainActivity : ComponentActivity() {

    /**
     * Se llama cuando se crea la actividad.
     *
     * Se encarga de inicializar la interfaz de usuario, configurar la base de datos,
     * el ViewModelFactory y el grafo de navegación con Jetpack Compose.
     *
     * @param savedInstanceState Si la actividad se reinicia después de haber sido cerrada,
     * este Bundle contiene los datos que proporcionó más recientemente en onSaveInstanceState(Bundle).
     * En caso contrario, es nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val database = ParkingDatabase.getDatabase(applicationContext)
            val dao = database.dao()
            val navController = rememberNavController()

            // Fábrica para crear instancias de los ViewModels con sus dependencias.
            val viewModelFactory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return when {
                        modelClass.isAssignableFrom(UsuarioViewModel::class.java) -> UsuarioViewModel(dao) as T
                        modelClass.isAssignableFrom(ParkingViewModel::class.java) -> ParkingViewModel(dao) as T
                        modelClass.isAssignableFrom(PagoViewModel::class.java) -> PagoViewModel(dao) as T
                        else -> throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            }

            val usuarioViewModel: UsuarioViewModel = viewModel(factory = viewModelFactory)
            val parkingViewModel: ParkingViewModel = viewModel(factory = viewModelFactory)
            val pagoViewModel: PagoViewModel = viewModel(factory = viewModelFactory)

            ParkingSmartTheme {
                // Configuración del grafo de navegación de la aplicación.
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
                            usuarioViewModel = usuarioViewModel,
                            onEspacioSeleccionado = {
                                navController.navigate("parking_camera")
                            }
                        )
                    }

                    composable("parking_camera") {
                        ParkingMainScreen(
                            navController = navController,
                            viewModel = parkingViewModel
                        )
                    }

                    composable("parking") {
                        ParkingScreen(
                            parkingViewModel = parkingViewModel,
                            usuarioViewModel = usuarioViewModel,
                            onNavegarAlPago = { monto, tiempo, correo ->
                                navController.navigate("metodopago/$monto/$tiempo/$correo")
                            }
                        )
                    }

                    composable(
                        route = "metodopago/{monto}/{tiempo}/{correo}",
                        arguments = listOf(
                            navArgument("monto") { type = NavType.FloatType },
                            navArgument("tiempo") { type = NavType.StringType },
                            navArgument("correo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val monto = backStackEntry.arguments?.getFloat("monto")?.toDouble() ?: 0.0
                        val tiempo = backStackEntry.arguments?.getString("tiempo") ?: "00:00"
                        val correo = backStackEntry.arguments?.getString("correo") ?: ""

                        MetodoPagoScreen(
                            viewModel = pagoViewModel,
                            monto = monto,
                            tiempo = tiempo,
                            correoUsuario = correo,
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

    /**
     * Vista previa para la navegación de la aplicación en el editor de Android Studio.
     *
     * Esta función se utiliza para previsualizar el tema de la aplicación.
     * La navegación real no se previsualiza aquí debido a la complejidad de la
     * inicialización de los ViewModels.
     */
    @Preview(showBackground = true)
    @Composable
    fun AppNavigationPreview() {
        val navController = rememberNavController()

        ParkingSmartTheme {
            // Simplificado para la vista previa si es necesario, o mejor, no usarlo si depende de una fábrica compleja.
        }
    }
}
