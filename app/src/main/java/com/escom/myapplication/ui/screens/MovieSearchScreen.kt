package com.escom.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.escom.myapplication.model.User
import com.escom.myapplication.ui.components.MovieItem
import com.escom.myapplication.viewmodel.MovieViewModel
import com.escom.myapplication.viewmodel.AuthViewModel

@Composable
fun MovieSearchScreen(
    movieViewModel: MovieViewModel, 
    authViewModel: AuthViewModel,
    user: User, 
    onLogout: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by movieViewModel.searchResults.observeAsState(emptyList())
    val loading by movieViewModel.loading.observeAsState(false)
    val error by movieViewModel.error.observeAsState()
    val movieDetail by movieViewModel.movieDetail.observeAsState()
    val context = LocalContext.current
    
    // Estados para controlar la navegación
    var showingDetails by remember { mutableStateOf(false) }
    var showingFavorites by remember { mutableStateOf(false) }
    var showingUserManagement by remember { mutableStateOf(false) }
    var showingSearchHistory by remember { mutableStateOf(false) }  // Historial del usuario
    var showingAllFavorites by remember { mutableStateOf(false) }  // Todos los favoritos
    var showingAllSearchHistory by remember { mutableStateOf(false) }  // Nuevo estado para historial de todos
    
    when {
        showingUserManagement -> {
            UserManagementScreen(
                authViewModel = authViewModel,
                onBackPressed = { showingUserManagement = false }
            )
        }
        showingSearchHistory -> {
            SearchHistoryScreen(
                movieViewModel = movieViewModel,
                user = user,
                onBackPressed = { showingSearchHistory = false }
            )
        }
        showingAllSearchHistory -> {  // Nuevo caso para mostrar el historial de todos
            AllSearchHistoryScreen(
                movieViewModel = movieViewModel,
                user = user,
                onBackPressed = { showingAllSearchHistory = false }
            )
        }
        showingFavorites -> {
            FavoritesScreen(
                movieViewModel = movieViewModel,
                user = user,
                onBackPressed = { showingFavorites = false },
                onMovieClick = { imdbId ->
                    movieViewModel.getMovieDetails(imdbId)
                    showingFavorites = false
                    showingDetails = true
                }
            )
        }
        showingAllFavorites -> {
            AllFavoritesScreen(
                movieViewModel = movieViewModel,
                user = user,
                onBackPressed = { showingAllFavorites = false }
            )
        }
        showingDetails && movieDetail != null -> {
            // Mostrar pantalla de detalles
            MovieDetailScreen(
                movieViewModel = movieViewModel,
                movieDetail = movieDetail!!,
                user = user,
                onBackPressed = { 
                    showingDetails = false 
                    movieViewModel.clearMovieDetail()
                }
            )
        }
        else -> {
            // Pantalla principal de búsqueda con mejor organización
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header con información del usuario y acciones
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Información del usuario
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Hola, ${user.firstName}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                Text(
                                    text = "Rol: ${user.role}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Botones de acción
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Primera fila de botones
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Botón para gestión de usuarios (solo para administradores)
                                    if (user.role == "ADMIN") {
                                        TextButton(
                                            onClick = { showingUserManagement = true },
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 4.dp)
                                        ) {
                                            Text("Gestionar Usuarios")
                                        }
                                    } else {
                                        // Espacio vacío para mantener la simetría cuando no hay botón de admin
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    
                                    // Botón para ver historial
                                    TextButton(
                                        onClick = { showingSearchHistory = true },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 4.dp)
                                    ) {
                                        Text("Historial")
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                // Segunda fila de botones
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Botón para ver favoritos
                                    TextButton(
                                        onClick = { showingFavorites = true },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 4.dp)
                                    ) {
                                        Text("Favoritos")
                                    }
                                    
                                    // Botón para cerrar sesión
                                    TextButton(
                                        onClick = onLogout,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 4.dp),
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("Cerrar Sesión")
                                    }
                                }
                                
                                // Botón para ver todos los favoritos (solo para administradores)
                                if (user.role == "ADMIN") {
                                    Button(
                                        onClick = { showingAllFavorites = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Ver Favoritos de Todos los Usuarios")
                                    }
                                    
                                    // Añadir botón para ver historial de todos los usuarios
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { showingAllSearchHistory = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Ver Historial de Todos los Usuarios")
                                    }
                                }
                            }
                        }
                    }
                    
                    // Barra de búsqueda mejorada
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Buscar Películas",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Título de la película") },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        if (searchQuery.isNotEmpty()) {
                                            movieViewModel.searchMovies(searchQuery, 1, user.id)
                                        }
                                    }) {
                                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(onSearch = {
                                    if (searchQuery.isNotEmpty()) {
                                        movieViewModel.searchMovies(searchQuery, 1, user.id)
                                    }
                                }),
                                singleLine = true
                            )
                        }
                    }
                    
                    // Área de resultados
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Mostrar resultados de búsqueda
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else if (searchResults.isNotEmpty()) {
                                Column {
                                    Text(
                                        text = "Resultados de búsqueda",
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    
                                    LazyColumn {
                                        items(searchResults) { movie ->
                                            MovieItem(movie, onClick = {
                                                movieViewModel.getMovieDetails(movie.imdbID)
                                                showingDetails = true
                                            })
                                        }
                                    }
                                }
                            } else if (searchQuery.isNotEmpty()) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No se encontraron resultados",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "para '$searchQuery'",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Busca películas",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Ingresa un título para ver resultados",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
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
    }
}