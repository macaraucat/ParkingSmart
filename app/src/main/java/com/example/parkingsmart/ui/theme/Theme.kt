package com.example.parkingsmart.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Paleta de colores para el tema oscuro.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

/**
 * Paleta de colores para el tema claro.
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * Tema principal de la aplicación ParkingSmart.
 *
 * Este composable envuelve el contenido de la aplicación y aplica el tema de Material Design 3.
 * Admite temas claros y oscuros, así como colores dinámicos en Android 12 y versiones posteriores.
 *
 * @param darkTheme `true` si se debe usar el tema oscuro, `false` para el tema claro. El valor predeterminado es el tema del sistema.
 * @param dynamicColor `true` para usar colores dinámicos (si están disponibles), `false` para usar los colores definidos en la aplicación. Habilitado por defecto.
 * @param content El contenido de la interfaz de usuario al que se le aplicará el tema.
 */
@Composable
fun ParkingSmartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // El color dinámico está disponible en Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
