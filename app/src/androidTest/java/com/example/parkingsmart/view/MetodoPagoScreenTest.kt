package com.example.parkingsmart.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.parkingsmart.repository.ParkingDao
import com.example.parkingsmart.viewmodel.PagoViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class MetodoPagoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dao: ParkingDao = mockk(relaxed = true)
    private val viewModel = PagoViewModel(dao)

    @Test
    fun metodoPagoScreen_muestraMontoCorrecto() {
        val monto = 5000.0
        val tiempo = "1:30"

        composeTestRule.setContent {
            MetodoPagoScreen(
                viewModel = viewModel,
                monto = monto,
                tiempo = tiempo,
                correoUsuario = "test@user.com",
                onPagoExitoso = {}
            )
        }

        // Verificar que el título y el monto se muestran
        composeTestRule.onNodeWithText("Método de Pago").assertIsDisplayed()
        composeTestRule.onNodeWithText("Total a pagar: $5000").assertIsDisplayed()
    }

    @Test
    fun seleccionarEfectivo_muestraIndicadorCarga() {
        composeTestRule.setContent {
            MetodoPagoScreen(
                viewModel = viewModel,
                monto = 1000.0,
                tiempo = "0:10",
                correoUsuario = "test@user.com",
                onPagoExitoso = {}
            )
        }

        // Hacer clic en Efectivo
        composeTestRule.onNodeWithText("Efectivo").performClick()

        // Verificar que aparece el texto de procesamiento (el CircularProgressIndicator no tiene texto, así que buscamos el texto adjunto)
        composeTestRule.onNodeWithText("Procesando pago...").assertIsDisplayed()
    }

    @Test
    fun botones_seMuestranCuandoNoEstaProcesando() {
        composeTestRule.setContent {
            MetodoPagoScreen(
                viewModel = viewModel,
                monto = 2000.0,
                tiempo = "0:30",
                correoUsuario = "test@user.com",
                onPagoExitoso = {}
            )
        }

        // Verificar que los botones están presentes inicialmente
        composeTestRule.onNodeWithText("Efectivo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tarjeta de Crédito/Débito").assertIsDisplayed()
    }
}
