package com.example.parkingsmart.viewmodel

import com.example.parkingsmart.repository.ParkingDao
import com.example.parkingsmart.repository.TicketEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PagoViewModelTest {

    private val dao: ParkingDao = mockk()
    private lateinit var viewModel: PagoViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PagoViewModel(dao)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `inicializarPago actualiza el monto y tiempo en el estado`() {
        viewModel.inicializarPago(5000.0, "1:20")
        
        val state = viewModel.uiState.value
        assertEquals(5000.0, state.montoTotal)
        assertEquals("1:20", state.tiempoTotal)
    }

    @Test
    fun `seleccionarMetodoPago actualiza el metodo en el estado`() {
        viewModel.seleccionarMetodoPago("Tarjeta de Credito")
        
        val state = viewModel.uiState.value
        assertEquals("Tarjeta de Credito", state.metodoPagoSeleccionado)
    }

    @Test
    fun `procesarPago exitoso actualiza el estado a pagoExitoso`() = runTest {
        val correo = "test@correo.com"
        val ticketActivo = TicketEntity(
            id = 1,
            usuarioCorreo = correo,
            patente = "ABCD12",
            numeroEspacio = 5,
            horaEntrada = System.currentTimeMillis()
        )

        // Mock del DAO
        coEvery { dao.obtenerTicketActivo(correo) } returns ticketActivo
        coEvery { dao.actualizarTicket(any()) } returns Unit

        viewModel.inicializarPago(3000.0, "0:45")
        viewModel.procesarPago(correo)

        // Verificar estado inicial de procesamiento
        assertTrue(viewModel.uiState.value.estaProcesando)

        // Avanzar el tiempo por el delay(1500)
        advanceTimeBy(1600)

        val state = viewModel.uiState.value
        assertFalse(state.estaProcesando)
        assertTrue(state.pagoExitoso)
        assertNull(state.mensajeError)

        coVerify { dao.actualizarTicket(match { it.estado == "PAGADO" && it.costoTotal == 3000.0 }) }
    }

    @Test
    fun `procesarPago falla cuando no hay ticket activo`() = runTest {
        val correo = "test@correo.com"
        coEvery { dao.obtenerTicketActivo(correo) } returns null

        viewModel.procesarPago(correo)
        advanceTimeBy(1600)

        val state = viewModel.uiState.value
        assertFalse(state.estaProcesando)
        assertFalse(state.pagoExitoso)
        assertEquals("No se encontró un ticket activo.", state.mensajeError)
    }

    @Test
    fun `reiniciarEstado vuelve el estado a los valores por defecto`() {
        viewModel.inicializarPago(1000.0, "0:10")
        viewModel.reiniciarEstado()

        val state = viewModel.uiState.value
        assertEquals(0.0, state.montoTotal)
        assertEquals("0:00", state.tiempoTotal)
        assertFalse(state.pagoExitoso)
    }
}
