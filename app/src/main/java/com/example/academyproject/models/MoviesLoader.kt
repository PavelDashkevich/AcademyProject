package com.example.academyproject.models

import com.example.academyproject.network.JsonConfiguration
import com.example.academyproject.network.JsonGenresResponse
import com.example.academyproject.network.theMovieDbApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MoviesLoader(private var handler: MoviesLoaderSubscriber? = null) {
    fun requestMoviesList() {
        CoroutineScope(Dispatchers.Main).launch { loadMoviesList() }
    }

    fun requestMovieDetails(movieID: Int) {
        CoroutineScope(Dispatchers.Main).launch { loadMovieDetails(movieID) }
    }

    fun requestMovieCredits(movieID: Int) {
        CoroutineScope(Dispatchers.Main).launch { loadMovieCredits(movieID) }
    }

    private suspend fun loadMoviesList() {
        var baseUrl = ""
        var errorMsg = ""
        var movies: List<Movie> = listOf()
        lateinit var configuration: JsonConfiguration

        try {
            configuration = theMovieDbApiService.getConfiguration()
            baseUrl = configuration.images.baseUrl

            val jsonGenresList: JsonGenresResponse = theMovieDbApiService.getGenres()
            val jsonGenres = jsonGenresList.genres
            val genres = jsonGenres.map { Genre(id = it.id, name = it.name) }

            val genresMap = genres.associateBy { it.id }
            val topRatedMoviesResponse = theMovieDbApiService.getTopRatedMovies()
            movies = topRatedMoviesResponse.results.map { jsonMovie ->
                val movie = Movie(
                    jsonMovie = jsonMovie,
                    genres = jsonMovie.genreIds.map {
                        genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                    },
                    actors = emptyList()
                )
                movie.applyBaseUrl(configuration.images.baseUrl)
                movie
            }
        } catch (e: Exception) {
            errorMsg = if (e is HttpException) {
                "Error occurred: code ${e.code()}: ${e.message()}"
            } else {
                "Error occurred: ${e.message ?: ""}"
            }
        } finally {
            handler?.onMoviesLoaded(movies, baseUrl, errorMsg)
        }
    }

    private suspend fun loadMovieDetails(movieID: Int) {
        val jsonMovie = theMovieDbApiService.getMovieDetails(movieID)
        handler?.onMovieDetailsLoaded(Movie(jsonMovie, emptyList(), emptyList()))
    }

    private suspend fun loadMovieCredits(movieID: Int) {
        val movieCreditsResponse = theMovieDbApiService.getMovieCredits(movieID)
        val actors = movieCreditsResponse.cast.map {
            Actor(id = it.id, name = it.name, picture = it.imageUrl)
        }

        handler?.onMovieCreditsLoaded(movieID, actors)
    }
}