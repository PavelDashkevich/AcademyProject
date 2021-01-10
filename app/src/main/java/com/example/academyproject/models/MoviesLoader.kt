package com.example.academyproject.models

import android.content.Context
import com.example.academyproject.models.data.Movie
import com.example.academyproject.models.data.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesLoader(private var handler: MoviesLoaderSubscriber? = null) {
    fun requestMoviesList(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            loadMoviesList(context)
        }
    }

    private suspend fun loadMoviesList(context: Context) {
        val movies = loadMovies(context)
        handler?.onMoviesLoaded(movies)
    }
}

interface MoviesLoaderSubscriber {
    fun onMoviesLoaded(movies: List<Movie>)
}