package com.example.academyproject.models

import android.util.Log
import com.example.academyproject.caching.Cacher
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            Log.d("MovieApp", "MoviesLoader: requestMoviesList(): repository.getAllMovies() called")

            if (movies.isNotEmpty()) {
                Log.d("MovieApp", "MoviesLoader: requestMoviesList(): movies list is not empty")
                handler?.onMoviesLoaded(movies, errorMsg)
                return@launch
            }

            Log.d("MovieApp", "MoviesLoader: requestMoviesList(): movies list is empty, trying to load from network to cache")

            try {
                movies = cacher.loadMoviesList()
            } catch (e: Exception) {
                errorMsg = if (e is HttpException) {
                    "Error occurred: code ${e.code()}: ${e.message()}"
                } else {
                    "Error occurred: ${e.message ?: ""}"
                }

                Log.d("MovieApp", "MoviesLoader: requestMoviesList(): exception: $errorMsg")
            } finally {
                Log.d("MovieApp", "MoviesLoader: requestMoviesList(): end of call")
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
                val errorMsg = if (e is HttpException) {
                    "Error occurred: code ${e.code()}: ${e.message()}"
                } else {
                    "Error occurred: ${e.message ?: ""}"
                }

                Log.d("MovieApp", "MoviesLoader: requestMovieDetails(): exception: $errorMsg")
            }
        }
    }

    fun requestMoviesFromFlow(viewModelScope: CoroutineScope) {
        Log.d("MovieApp", "MoviesLoader: requestMoviesFromFlow()")
        viewModelScope.launch {
            repository.getAllMoviesAsFlow().collect {
                Log.d("MovieApp", "MoviesLoader: requestMoviesFromFlow(): collect")
                handler?.onMoviesFromFlowLoaded(it)
                Log.d("MovieApp", "MoviesLoader: requestMoviesFromFlow(): collect (after sending to handle)")
            }
            Log.d("MovieApp", "MoviesLoader: requestMoviesFromFlow(): after collect")
        }
    }
}