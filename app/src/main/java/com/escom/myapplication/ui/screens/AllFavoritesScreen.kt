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
import androidx.compose.ui.unit.dp
import com.escom.myapplication.model.Movie
import com.escom.myapplication.model.User
import com.escom.myapplication.viewmodel.MovieViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage

@Composable
fun AllFavoritesScreen(
    movieViewModel: MovieViewModel,
    user: User,
    onBackPressed: () -> Unit
) {
    // Modificar la verificación de administrador para usar role en lugar de isAdmin
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
    
    val allFavorites by movieViewModel.allFavoriteMovies.observeAsState(emptyList())
    val loading by movieViewModel.loading.observeAsState(false)
    
    // Cargar todos los favoritos cuando se muestra la pantalla
    LaunchedEffect(Unit) {
        movieViewModel.getAllFavoriteMovies()
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
                    text = "Favoritos de Todos los Usuarios",
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
            } else if (allFavorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay películas favoritas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(allFavorites) { movie ->
                        AllFavoritesItem(movie)
                    }
                }
            }
        }
    }
}

@Composable
fun AllFavoritesItem(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la película
            AsyncImage(
                model = if (movie.posterUrl != "N/A") movie.posterUrl else "https://via.placeholder.com/100x150?text=No+Poster",
                contentDescription = "Poster de ${movie.title}",
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Año: ${movie.year}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Director: ${movie.director}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Género: ${movie.genre}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Usuario ID: ${movie.userId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}