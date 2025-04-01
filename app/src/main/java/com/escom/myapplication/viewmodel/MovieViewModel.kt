package com.escom.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escom.myapplication.model.Movie
import com.escom.myapplication.model.MovieDetail
import com.escom.myapplication.model.MovieSearchResponse
import com.escom.myapplication.model.MovieSearchResult
import com.escom.myapplication.model.SearchHistory
import com.escom.myapplication.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(context: Context) : ViewModel() {
    private val movieRepository = MovieRepository(context)

    private val _searchResults = MutableLiveData<List<MovieSearchResult>>()
    val searchResults: LiveData<List<MovieSearchResult>> = _searchResults

    private val _totalResults = MutableLiveData<Int>(0)
    val totalResults: LiveData<Int> = _totalResults

    private val _currentPage = MutableLiveData<Int>(1)
    val currentPage: LiveData<Int> = _currentPage

    private val _movieDetail = MutableLiveData<MovieDetail?>()
    val movieDetail: LiveData<MovieDetail?> = _movieDetail

    private val _favoriteMovies = MutableLiveData<List<Movie>>(emptyList())
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies

    private val _searchHistory = MutableLiveData<List<SearchHistory>>(emptyList())
    val searchHistory: LiveData<List<SearchHistory>> = _searchHistory

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun searchMovies(title: String, page: Int, userId: Long) {
        _loading.value = true
        _error.value = null
        _currentPage.value = page

        viewModelScope.launch {
            val result = movieRepository.searchMovies(title, page, userId)
            _loading.value = false

            result.fold(
                onSuccess = { response ->
                    _searchResults.value = response.Search ?: emptyList()
                    _totalResults.value = response.totalResults?.toIntOrNull() ?: 0
                },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun getMovieDetails(imdbId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = movieRepository.getMovieDetails(imdbId)
            _loading.value = false

            result.fold(
                onSuccess = { _movieDetail.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun saveFavoriteMovie(movie: Map<String, Any>) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = movieRepository.saveFavoriteMovie(movie)
            _loading.value = false

            result.fold(
                onSuccess = { 
                    // Asegurarse de que el userId sea un Long
                    val userId = movie["userId"]?.toString()?.toLongOrNull() ?: 0L
                    getFavoriteMovies(userId)
                },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun getFavoriteMovies(userId: Long) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = movieRepository.getFavoriteMovies(userId)
            _loading.value = false

            result.fold(
                onSuccess = { _favoriteMovies.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun removeFavoriteMovie(id: Long, userId: Long) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = movieRepository.removeFavoriteMovie(id)
            _loading.value = false

            result.fold(
                onSuccess = { getFavoriteMovies(userId) },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun getSearchHistory(userId: Long) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = movieRepository.getSearchHistory(userId)
            _loading.value = false

            result.fold(
                onSuccess = { _searchHistory.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearMovieDetail() {
        _movieDetail.value = null
    }
}