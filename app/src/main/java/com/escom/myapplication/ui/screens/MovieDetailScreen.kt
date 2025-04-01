package com.escom.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Changed import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.escom.myapplication.model.MovieDetail
import com.escom.myapplication.model.User
import com.escom.myapplication.ui.components.DetailItem
import com.escom.myapplication.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieViewModel: MovieViewModel,
    movieDetail: MovieDetail,
    user: User,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val error by movieViewModel.error.observeAsState()
    val favoriteMovies by movieViewModel.favoriteMovies.observeAsState(emptyList())
    
    // Verificar si la película ya está en favoritos
    val isAlreadyFavorite = favoriteMovies.any { it.movieId == movieDetail.imdbID }
    
    // Cargar favoritos al entrar a la pantalla
    LaunchedEffect(Unit) {
        movieViewModel.getFavoriteMovies(user.id)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Botón para volver atrás
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") // Changed usage
        }
        
        // Título de la película
        Text(
            text = movieDetail.Title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        // Imagen y detalles principales
        Row(modifier = Modifier.fillMaxWidth()) {
            // Poster de la película
            AsyncImage(
                model = if (movieDetail.Poster != "N/A") movieDetail.Poster else "https://via.placeholder.com/300x450?text=No+Poster",
                contentDescription = "Poster de ${movieDetail.Title}",
                modifier = Modifier
                    .width(150.dp)
                    .height(225.dp),
                contentScale = ContentScale.Crop
            )
            
            // Detalles básicos
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                DetailItem("Año", movieDetail.Year)
                DetailItem("Género", movieDetail.Genre)
                DetailItem("Director", movieDetail.Director)
                DetailItem("Duración", movieDetail.Runtime)
                DetailItem("Calificación", movieDetail.Rated)
                DetailItem("IMDb", movieDetail.imdbRating)
            }
        }
        
        // Sinopsis
        Text(
            text = "Sinopsis",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        Text(
            text = movieDetail.Plot,
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Actores
        Text(
            text = "Reparto",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = movieDetail.Actors,
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Botón para guardar como favorito o eliminar de favoritos
        if (isAlreadyFavorite) {
            // Encontrar el ID de la película favorita para poder eliminarla
            val favoriteId = favoriteMovies.find { it.movieId == movieDetail.imdbID }?.id ?: 0
            
            Button(
                onClick = {
                    movieViewModel.removeFavoriteMovie(favoriteId, user.id)
                    Toast.makeText(context, "Película eliminada de favoritos", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar de Favoritos")
            }
        } else {
            Button(
                onClick = {
                    // Extraer el año como número entero
                    val yearStr = movieDetail.Year.replace("[^0-9]".toRegex(), "")
                    val year = if (yearStr.isNotEmpty()) yearStr.toInt() else 0
                    
                    val movieData = mapOf(
                        "movieId" to movieDetail.imdbID,
                        "userId" to user.id.toString(),
                        "title" to movieDetail.Title,
                        "year" to year.toString(),
                        "director" to movieDetail.Director,
                        "genre" to movieDetail.Genre,
                        "plot" to movieDetail.Plot,
                        "posterUrl" to movieDetail.Poster
                    )
                    movieViewModel.saveFavoriteMovie(movieData)
                    Toast.makeText(context, "Película guardada como favorita", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text("Guardar como Favorita")
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