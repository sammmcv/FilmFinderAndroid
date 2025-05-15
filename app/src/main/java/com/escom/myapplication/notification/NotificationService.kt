package com.escom.myapplication.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

class NotificationService(private val context: Context) {

    fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Canal Diario"
            val descripcion = "Notificación diaria de FilmFinder"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel("daily_channel", nombre, importancia).apply {
                description = descripcion
            }
            val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(canal)
        }
    }

    fun programarNotificacionDiaria() {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8) // horas, en formato de 24 horas
            set(Calendar.MINUTE, 15)
            set(Calendar.SECOND, 20)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1 * 60 * 1000L, // 10 minutos en milisegundos
            pendingIntent
        )
    }
}