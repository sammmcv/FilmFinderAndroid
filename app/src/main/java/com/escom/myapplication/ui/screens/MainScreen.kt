package com.escom.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.escom.myapplication.viewmodel.AuthViewModel
import com.escom.myapplication.viewmodel.MovieViewModel

@Preview
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(context) }
    val movieViewModel = remember { MovieViewModel(context) }
    
    val user by authViewModel.user.observeAsState()
    val loading by authViewModel.loading.observeAsState(false)
    val error by authViewModel.error.observeAsState()
    
    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            
            if (user != null) {
                // Usuario autenticado, mostrar pantalla principal
                MovieSearchScreen(
                    movieViewModel = movieViewModel,
                    authViewModel = authViewModel,
                    user = user!!,
                    onLogout = { authViewModel.logout() }
                )
            } else {
                // Usuario no autenticado, mostrar pantalla de login o registro
                var showLoginScreen by remember { mutableStateOf(true) }
                
                if (showLoginScreen) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onNavigateToRegister = { showLoginScreen = false }
                    )
                } else {
                    RegisterScreen(
                        authViewModel = authViewModel,
                        onNavigateToLogin = { showLoginScreen = true }
                    )
                }
            }
            
            // Mostrar errores si los hay
            error?.let {
                LaunchedEffect(it) {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    authViewModel.clearError()
                }
            }
        }
    }
}