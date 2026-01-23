package com.example.parkingsmart.view

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.Window
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Realiza una captura de pantalla de la ventana actual.
 *
 * Utiliza [PixelCopy] para crear un [Bitmap] de la vista raíz de la ventana. Si tiene éxito,
 * guarda el bitmap en la galería. Muestra un [Toast] para indicar el éxito o el fracaso.
 *
 * @param window La [Window] de la que se tomará la captura de pantalla.
 * @param context El [Context] actual, utilizado para mostrar Toasts.
 */
fun takeScreenshot(window: Window, context: Context) {
    val view = window.decorView.rootView
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    try {
        PixelCopy.request(
            window,
            android.graphics.Rect(0, 0, view.width, view.height),
            bitmap,
            { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    saveBitmapToGallery(context, bitmap)
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Falló la captura", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            Handler(Looper.getMainLooper())
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Guarda un [Bitmap] en la galería de imágenes del dispositivo.
 *
 * Esta función maneja las diferencias entre las versiones de Android para el almacenamiento de medios.
 * Para Android Q (API 29) y superior, utiliza [MediaStore] con el API de Scoped Storage.
 * Para versiones anteriores, guarda directamente en el directorio público de imágenes y utiliza
 * [MediaScannerConnection] para que el archivo sea visible en la galería.
 *
 * @param context El [Context] actual, utilizado para acceder al ContentResolver.
 * @param bitmap El [Bitmap] que se guardará.
 */
fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
    val filename = "FOTO_${System.currentTimeMillis()}.jpg"
    var fos: OutputStream? = null

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MiAppCamara")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                fos = resolver.openOutputStream(it)
                fos?.let { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }

                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)
            }
        } else {
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val myDir = File(directory, "MiAppCamara")
            if (!myDir.exists()) myDir.mkdirs()

            val file = File(myDir, filename)
            fos = FileOutputStream(file)

            fos?.let { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }

            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
        }

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Guardado en Galería!", Toast.LENGTH_SHORT).show()
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } finally {
        fos?.close()
    }
}
