package com.escom.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.escom.myapplication.model.MovieSearchResult

@Composable
fun MovieItem(movie: MovieSearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Añadir imagen de la película
            AsyncImage(
                model = if (movie.Poster != "N/A") movie.Poster else "https://via.placeholder.com/100x150?text=No+Poster",
                contentDescription = "Poster de ${movie.Title}",
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.Title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Año: ${movie.Year}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Tipo: ${movie.Type}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}