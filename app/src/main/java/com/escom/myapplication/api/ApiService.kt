package com.escom.myapplication.api

import com.escom.myapplication.model.Movie
import com.escom.myapplication.model.MovieDetail
import com.escom.myapplication.model.MovieSearchResponse
import com.escom.myapplication.model.SearchHistory
import com.escom.myapplication.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Autenticación
    @POST("api/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<User>
    
    @POST("api/register")
    suspend fun register(@Body userData: Map<String, String>): Response<User>
    
    // Búsqueda de películas
    @GET("api/movies/search")
    suspend fun searchMovies(
        @Query("title") title: String,
        @Query("page") page: Int = 1,
        @Query("userId") userId: Long
    ): Response<MovieSearchResponse>
    
    // Obtener detalles de una película
    @GET("api/movies/{imdbId}")
    suspend fun getMovieDetails(
        @Path("imdbId") imdbId: String
    ): Response<MovieDetail>
    
    // Guardar película como favorita
    @POST("api/movies/favorites")
    suspend fun saveFavoriteMovie(@Body movieData: Map<String, String>): Response<Map<String, String>>
    
    // Obtener películas favoritas de un usuario
    @GET("api/movies/favorites/{userId}")
    suspend fun getFavoriteMovies(
        @Path("userId") userId: Long
    ): Response<List<Movie>>
    
    // Eliminar película favorita
    @DELETE("api/movies/favorites/{id}")
    suspend fun removeFavoriteMovie(
        @Path("id") id: Long
    ): Response<Map<String, String>>
    
    // Obtener historial de búsqueda
    @GET("api/history/{userId}")
    suspend fun getSearchHistory(
        @Path("userId") userId: Long
    ): Response<List<SearchHistory>>
    
    // User management endpoints
    @GET("api/users")
    suspend fun getAllUsers(): Response<List<User>>
    
    @POST("api/users/{id}/role")
    suspend fun updateUserRole(
        @Path("id") userId: Long,
        @Body roleData: Map<String, String>
    ): Response<Void>
    
    @DELETE("api/users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: Long
    ): Response<Void>
}