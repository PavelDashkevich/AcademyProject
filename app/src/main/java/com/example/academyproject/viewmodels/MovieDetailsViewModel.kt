package com.example.academyproject.viewmodels

import android.util.Log
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
    var movieGotById = MutableLiveData(false)

    fun selectMovie(movie: Movie) {
        this.movie = movie

        Log.d("MovieApp", "MovieDetailsViewModel: selectMovie($movie)")
        movieGotById.value = true

        this.movie?.let {
            if (!it.actorsLoaded) {
                actorsUpdatedInMovie.value = false
                model.requestMovieCredits(it.id)
            }
        }
    }

    fun selectMovieById(movieId: Int) {
        Log.d("MovieApp", "MovieDetailsViewModel: selectMovieById($movieId)")
        movieGotById.value = false
        model.requestMovieById(movieId)
    }

    fun getSelectedMovie(): Movie? = this.movie

    override fun onMovieCreditsLoaded(movieId: Int, actors: List<Actor>) {
        Log.d("MovieApp", "MovieDetailsViewModel: onMovieCreditsLoaded($movieId): $actors")
        movie?.let {
            if (it.id == movieId) {
                it.actors = actors
                it.actorsLoaded = true
                actorsUpdatedInMovie.value = true
            }
        }
    }

    override fun onMovieLoaded(movie: Movie?) {
        Log.d("MovieApp", "MovieDetailsViewModel: onMovieLoaded($movie)")
        movie?.let { selectMovie(it) }
    }
}