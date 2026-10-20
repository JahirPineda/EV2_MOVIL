package cl.duoc.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Ayudante sencillo para crear y mostrar notificaciones del sistema en Android.
 */
object NotificationHelper {

    // Identificadores del canal de notificaciones (obligatorio desde Android 8.0)
    private const val CANAL_ID = "canal_bitacoras"
    private const val CANAL_NOMBRE = "Notificaciones de Bitácoras"
    private const val CANAL_DESCRIPCION = "Avisos sobre creación y actualización de bitácoras"

    // Crea el canal si aún no existe
    fun crearCanalNotificacion(context: Context) {
        val canal = NotificationChannel(
            CANAL_ID,
            CANAL_NOMBRE,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CANAL_DESCRIPCION
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(canal)
    }

    // Muestra una notificación en la barra de estado del celular
    @SuppressLint("MissingPermission")
    fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
        // Aseguramos que el canal esté creado
        crearCanalNotificacion(context)

        // Configuramos la notificación (icono, título, texto y prioridad)
        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Se cierra al hacer clic
            .build()

        try {
            // Un id único basado en el tiempo actual para que no se sobreescriban
            val idUnico = (System.currentTimeMillis() % 100000).toInt()
            NotificationManagerCompat.from(context).notify(idUnico, notificacion)
        } catch (e: Exception) {
            // Si el usuario no dio permisos de notificación en Android 13+, no se cae la app
        }
    }
}
