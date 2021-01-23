package com.example.academyproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academyproject.models.Actor
import com.example.academyproject.models.Movie
import com.example.academyproject.models.MovieDetailsLoader
import com.example.academyproject.models.MovieDetailsLoaderSubscriber
import com.example.academyproject.persistence.MoviesRepository

class MovieDetailsViewModel(
    repository: MoviesRepository
) : ViewModel(), MovieDetailsLoaderSubscriber {
    private var model = MovieDetailsLoader(this, repository)
    private var movie: Movie? = null
    var actorsUpdatedInMovie = MutableLiveData(false)

    fun selectMovie(movie: Movie) {
        this.movie = movie
        this.movie?.let {
            if (!it.actorsLoaded) {
                actorsUpdatedInMovie.value = false
                model.requestMovieCredits(it.id)
            }
        }
    }

    fun getSelectedMovie(): Movie? = this.movie

    override fun onMovieCreditsLoaded(movieID: Int, actors: List<Actor>) {
        movie?.let {
            if (it.id == movieID) {
                it.actors = actors
                it.actorsLoaded = true
                actorsUpdatedInMovie.value = true
            }
        }
    }
}