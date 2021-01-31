package com.example.academyproject.models

import android.util.Log
import com.example.academyproject.caching.Cacher
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MoviesLoader(
    private var handler: MoviesLoaderSubscriber? = null,
    private val repository: MoviesRepository
) {
    private val cacher = Cacher(theMovieDbApiService, repository)

    fun requestMoviesList() {
        CoroutineScope(Dispatchers.Main).launch {
            var errorMsg = ""
            var movies: List<Movie> = repository.getAllMovies()

            if (movies.isNotEmpty())
                handler?.onMoviesLoaded(movies, errorMsg)

            try {
                movies = cacher.loadMoviesList()
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
                val movie = cacher.loadMovieDetails(movieID)
                handler?.onMovieDetailsLoaded(movie)
            } catch (e: Exception) {

            }
        }
    }

    fun requestMoviesFromFlow() {
        CoroutineScope(Dispatchers.Main).launch {
            repository.getAllMoviesAsFlow().collect {
                handler?.onMoviesLoaded(it, "")
            }
        }
    }
}