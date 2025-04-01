package com.escom.myapplication.repository

import android.content.Context
import com.escom.myapplication.api.RetrofitClient
import com.escom.myapplication.model.Movie
import com.escom.myapplication.model.MovieDetail
import com.escom.myapplication.model.MovieSearchResponse
import com.escom.myapplication.model.SearchHistory

class MovieRepository(private val context: Context) {
    private val apiService = RetrofitClient.getApiService(context)
    
    suspend fun searchMovies(title: String, page: Int, userId: Long): Result<MovieSearchResponse> {
        return try {
            val response = apiService.searchMovies(title, page, userId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getMovieDetails(imdbId: String): Result<MovieDetail> {
        return try {
            val response = apiService.getMovieDetails(imdbId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun saveFavoriteMovie(movie: Map<String, Any>): Result<String> {
        return try {
            // Convertir el mapa a un mapa con valores String para evitar problemas de tipo
            val movieStringMap = mutableMapOf<String, String>()
            movie.forEach { (key, value) ->
                movieStringMap[key] = value.toString()
            }
            
            val response = apiService.saveFavoriteMovie(movieStringMap)
            if (response.isSuccessful) {
                Result.success(response.body()?.get("message") ?: "Película guardada como favorita")
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFavoriteMovies(userId: Long): Result<List<Movie>> {
        return try {
            val response = apiService.getFavoriteMovies(userId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeFavoriteMovie(id: Long): Result<String> {
        return try {
            val response = apiService.removeFavoriteMovie(id)
            if (response.isSuccessful) {
                Result.success(response.body()?.get("message") ?: "Película eliminada de favoritos")
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getSearchHistory(userId: Long): Result<List<SearchHistory>> {
        return try {
            val response = apiService.getSearchHistory(userId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}