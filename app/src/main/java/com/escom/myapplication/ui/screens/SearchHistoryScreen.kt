package com.escom.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.escom.myapplication.model.SearchHistory
import com.escom.myapplication.model.User
import com.escom.myapplication.viewmodel.MovieViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SearchHistoryScreen(
    movieViewModel: MovieViewModel,
    user: User,
    onBackPressed: () -> Unit
) {
    val searchHistory by movieViewModel.searchHistory.observeAsState(emptyList())
    val loading by movieViewModel.loading.observeAsState(false)
    
    // Cargar el historial cuando se muestra la pantalla
    LaunchedEffect(Unit) {
        movieViewModel.getSearchHistory(user.id)
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
                    text = "Historial de Búsquedas",
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
            } else if (searchHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay historial de búsquedas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Ordenar la lista para mostrar las búsquedas más recientes primero
                val sortedHistory = searchHistory.sortedByDescending { it.timestamp }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(sortedHistory) { item ->
                        SearchHistoryItem(item)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchHistoryItem(history: SearchHistory) {
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
                text = history.searchTerm,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = formatTimestamp(history.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Función para formatear la fecha y hora
private fun formatTimestamp(timestamp: String): String {
    return try {
        // Intentar parsear la fecha ISO
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(timestamp, formatter)
        
        // Formatear para mostrar de forma amigable
        val outputFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        // Si hay error en el formato, mostrar el string original
        timestamp
    }
}