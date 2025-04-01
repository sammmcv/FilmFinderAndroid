package com.escom.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Changed import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.escom.myapplication.model.User
import com.escom.myapplication.ui.components.FavoriteMovieItem
import com.escom.myapplication.viewmodel.MovieViewModel

@Composable
fun FavoritesScreen(
    movieViewModel: MovieViewModel,
    user: User,
    onBackPressed: () -> Unit,
    onMovieClick: (String) -> Unit
) {
    val favoriteMovies by movieViewModel.favoriteMovies.observeAsState(emptyList())
    val loading by movieViewModel.loading.observeAsState(false)
    val error by movieViewModel.error.observeAsState()
    val context = LocalContext.current
    
    // Cargar favoritos al entrar a la pantalla
    LaunchedEffect(Unit) {
        movieViewModel.getFavoriteMovies(user.id)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior con botón de volver
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") // Changed usage
            }
            
            Text(
                text = "Mis Películas Favoritas",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        // Mostrar lista de favoritos o mensajes
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (favoriteMovies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes películas favoritas guardadas",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(favoriteMovies) { movie ->
                    FavoriteMovieItem(
                        movie = movie,
                        onDelete = {
                            movieViewModel.removeFavoriteMovie(movie.id, user.id)
                            Toast.makeText(context, "Película eliminada de favoritos", Toast.LENGTH_SHORT).show()
                        },
                        onClick = {
                            // Navegar a detalles de la película
                            onMovieClick(movie.movieId)
                        }
                    )
                }
            }
        }
        
        // Mostrar errores si los hay
        error?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                movieViewModel.clearError()
            }
        }
    }
}