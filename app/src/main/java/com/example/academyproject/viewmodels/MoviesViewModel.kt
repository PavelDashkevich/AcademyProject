package com.example.academyproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academyproject.models.Movie
import com.example.academyproject.models.MoviesLoader
import com.example.academyproject.models.MoviesLoaderSubscriber
import com.example.academyproject.persistence.MoviesRepository

class MoviesViewModel(
    repository: MoviesRepository
) : ViewModel(), MoviesLoaderSubscriber {
    private var model = MoviesLoader(this, repository)

    var isMoviesLoading = MutableLiveData(false)
    var moviesList = MutableLiveData<List<Movie>>(mutableListOf())
    var latestUpdatedItemIndex = MutableLiveData<Int?>(null)
    var errorOnMoviesLoading = MutableLiveData("")

    fun loadMovies() {
        isMoviesLoading.value = true
        model.requestMoviesList()
    }

    fun loadMovieDetails(movieID: Int) {
        model.requestMovieDetails(movieID)
    }

    fun loadMoviesFromFlow() {
        model.requestMoviesFromFlow()
    }

    override fun onMoviesLoaded(movies: List<Movie>, errorMsg: String) {
        isMoviesLoading.value = false
        moviesList.value = movies
        errorOnMoviesLoading.value = errorMsg
    }

    override fun onMovieDetailsLoaded(movie: Movie) {
        var indexOfUpdatedItem: Int? = null
        val existingMovie: Movie? = getExistingMovieById(movie.id)

        existingMovie?.let {
            indexOfUpdatedItem = moviesList.value?.indexOf(it)
            it.runtime = movie.runtime
            it.runtimeLoaded = true
        }

        latestUpdatedItemIndex.value = indexOfUpdatedItem
    }

    private fun getExistingMovieById(movieID: Int): Movie? {
        var existingMovie: Movie? = null

        try {
            existingMovie = moviesList.value?.first { it.id == movieID }
        } catch (e: NoSuchElementException) { }

        return existingMovie
    }
}