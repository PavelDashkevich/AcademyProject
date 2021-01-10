package com.example.academyproject.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academyproject.models.MoviesLoader
import com.example.academyproject.models.MoviesLoaderSubscriber
import com.example.academyproject.models.data.Movie

class MoviesViewModel: ViewModel(), MoviesLoaderSubscriber {
    private var model = MoviesLoader(this)

    var isMoviesLoading = MutableLiveData(false)
    var moviesList = MutableLiveData<List<Movie>>(mutableListOf())
    var selectedMovie = MutableLiveData<Movie?>(null)

    fun loadMovies(context: Context) {
        isMoviesLoading.value = true
        model.requestMoviesList(context)
    }

    override fun onMoviesLoaded(movies: List<Movie>) {
        isMoviesLoading.value = false
        moviesList.value = movies
    }

    fun selectMovie(movieID: Int) {
        selectedMovie.value = moviesList.value?.first { it.id == movieID }
    }
}