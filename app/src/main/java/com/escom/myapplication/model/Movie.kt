package com.escom.myapplication.model

data class Movie(
    val id: Long,
    val movieId: String,
    val userId: Long,
    val title: String,
    val year: Int,
    val director: String,
    val genre: String,
    val plot: String,
    val posterUrl: String  // Make sure this property exists
)

// Clase para representar la respuesta de búsqueda de películas
data class MovieSearchResponse(
    val Search: List<MovieSearchResult>? = null,
    val totalResults: String? = null,
    val Response: String? = null,
    val Error: String? = null
)

data class MovieSearchResult(
    val Title: String = "",
    val Year: String = "",
    val imdbID: String = "",
    val Type: String = "",
    val Poster: String = ""
)

// Clase para representar los detalles de una película
data class MovieDetail(
    val Title: String = "",
    val Year: String = "",
    val Rated: String = "",
    val Released: String = "",
    val Runtime: String = "",
    val Genre: String = "",
    val Director: String = "",
    val Writer: String = "",
    val Actors: String = "",
    val Plot: String = "",
    val Language: String = "",
    val Country: String = "",
    val Awards: String = "",
    val Poster: String = "",
    val Ratings: List<Rating>? = null,
    val Metascore: String = "",
    val imdbRating: String = "",
    val imdbVotes: String = "",
    val imdbID: String = "",
    val Type: String = "",
    val DVD: String = "",
    val BoxOffice: String = "",
    val Production: String = "",
    val Website: String = "",
    val Response: String = ""
)

data class Rating(
    val Source: String = "",
    val Value: String = ""
)