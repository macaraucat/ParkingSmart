package com.example.parkingsmart.ui.theme.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

/**
 * Este archivo contiene funciones de utilidad relacionadas con el tamaño de la ventana.
 */

/**
 * Composable que calcula y devuelve la clase de tamaño de ventana para la actividad actual.
 *
 * Esta función utiliza `calculateWindowSizeClass` de Material 3 para determinar la
 * clase de tamaño de la ventana (por ejemplo, Compact, Medium, Expanded) basándose en las
 * dimensiones de la actividad actual. Esto es útil para crear diseños adaptables
 * que cambian según el tamaño de la pantalla.
 *
 * Se utiliza `@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)` porque la API
 * `calculateWindowSizeClass` todavía se considera experimental.
 *
 * @return La [WindowSizeClass] para la actividad actual.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalActivity.current as android.app.Activity)
}