package com.escom.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.escom.myapplication.notification.NotificationService
import com.escom.myapplication.ui.screens.MainScreen
import com.escom.myapplication.ui.theme.FilmFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Inicializar y usar el servicio de notificaciones
        val notificationService = NotificationService(this)
        notificationService.crearCanalNotificacion()
        notificationService.programarNotificacionDiaria()
        
        // Solicitar permisos en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }
        
        setContent {
            FilmFinderTheme {
                MainScreen()
            }
        }
    }
}