package com.example.academyproject.models

import com.example.academyproject.network.imageBaseUrl
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MoviesLoader(
    private var handler: MoviesLoaderSubscriber? = null,
    private val repository: MoviesRepository
) {
    fun requestMoviesList() {
        CoroutineScope(Dispatchers.Main).launch {
            var errorMsg = ""
            var movies: List<Movie> = repository.getAllMovies()

            if (movies.isNotEmpty())
                handler?.onMoviesLoaded(movies, errorMsg)

            try {
                val genres = theMovieDbApiService.getGenres().genres
                repository.replaceAllGenres(genres)

                val genresMap = genres.associateBy { it.id }
                imageBaseUrl = theMovieDbApiService.getConfiguration().images.baseUrl
                movies = theMovieDbApiService.getTopRatedMovies().results
                movies.forEach { movie ->
                    movie.applyImageBaseUrl(imageBaseUrl)
                    movie.genres = movie.genreIds.map {
                        genresMap[it] ?: Genre(it, "Unknown")
                    }
                }
                repository.insertMovies(movies)

            } catch (e: Exception) {
                errorMsg = if (e is HttpException) {
                    "Error occurred: code ${e.code()}: ${e.message()}"
                } else {
                    "Error occurred: ${e.message ?: ""}"
                }
            } finally {
                handler?.onMoviesLoaded(movies, errorMsg)
            }
        }
    }

    fun requestMovieDetails(movieID: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val movie = theMovieDbApiService.getMovieDetails(movieID)
                repository.updateRuntimeById(movie.runtime ?: 0, movie.id)
                handler?.onMovieDetailsLoaded(movie)
            } catch (e: Exception) {

            }
        }
    }
}