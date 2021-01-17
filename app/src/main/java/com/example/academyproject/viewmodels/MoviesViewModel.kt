package com.example.academyproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academyproject.models.Actor
import com.example.academyproject.models.MoviesLoader
import com.example.academyproject.models.MoviesLoaderSubscriber
import com.example.academyproject.models.Movie

class MoviesViewModel: ViewModel(), MoviesLoaderSubscriber {
    private var model = MoviesLoader(this)

    var isMoviesLoading = MutableLiveData(false)
    var moviesList = MutableLiveData<List<Movie>>(mutableListOf())
    var selectedMovie = MutableLiveData<Movie?>(null)
    var latestUpdatedItemIndex = MutableLiveData<Int?>(null)
    private var imagesBaseUrl = MutableLiveData("")
    var actorsUpdatedInMovieID = MutableLiveData<Int?>(null)
    var errorOnMoviesLoading = MutableLiveData("")

    fun loadMovies() {
        isMoviesLoading.value = true
        model.requestMoviesList()
    }

    fun selectMovie(movieID: Int) {
        try {
            selectedMovie.value = moviesList.value?.first { it.id == movieID }
        } catch (e: NoSuchElementException) {
            selectedMovie.value = null
        }

        selectedMovie.value?.let {
            if (!it.actorsLoaded) model.requestMovieCredits(it.id)
        }
    }

    fun loadMovieDetails(movieID: Int) {
        model.requestMovieDetails(movieID)
    }

    override fun onMoviesLoaded(movies: List<Movie>, imagesBaseUrl: String, errorMsg: String) {
        this.imagesBaseUrl.value = imagesBaseUrl
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

    override fun onMovieCreditsLoaded(movieID: Int, actors: List<Actor>) {
        val existingMovie: Movie? = getExistingMovieById(movieID)
        var idOfUpdatedItem: Int? = null

        existingMovie?.let {
            it.actors = actors
            it.actors.forEach { actor ->
                imagesBaseUrl.value?.let { baseUrl ->
                    actor.applyBaseUrl(baseUrl)
                }
            }
            it.actorsLoaded = true
            idOfUpdatedItem = movieID
        }

        actorsUpdatedInMovieID.value = idOfUpdatedItem
    }
}