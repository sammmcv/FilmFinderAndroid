package com.escom.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.escom.myapplication.model.SearchHistory
import com.escom.myapplication.model.User
import com.escom.myapplication.viewmodel.MovieViewModel

@Composable
fun AllSearchHistoryScreen(
    movieViewModel: MovieViewModel,
    user: User,
    onBackPressed: () -> Unit
) {
    // Only allow access to administrators
    if (user.role != "ADMIN") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Acceso denegado. Solo administradores.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    val allSearchHistory by movieViewModel.allSearchHistory.observeAsState(emptyList())
    val loading by movieViewModel.loading.observeAsState(false)
    val error by movieViewModel.error.observeAsState()
    val context = LocalContext.current

    // Load all search history when the screen is shown
    LaunchedEffect(Unit) {
        movieViewModel.getAllSearchHistory()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con título y botón de regreso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar"
                    )
                }
                
                Text(
                    text = "Historial de Búsqueda de Todos",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            Divider()
            
            // Contenido principal
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $error",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (allSearchHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay historial de búsqueda disponible",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(allSearchHistory) { history ->
                        AllSearchHistoryItem(history)
                    }
                }
            }
        }
    }
}

@Composable
fun AllSearchHistoryItem(history: SearchHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Búsqueda: ${history.searchTerm}",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Usuario ID: ${history.userId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${history.timestamp}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}