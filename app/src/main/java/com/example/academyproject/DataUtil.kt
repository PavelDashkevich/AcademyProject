package com.example.academyproject

import com.example.academyproject.data.Movie

class DataUtil {
    companion object {
        var isDataLoaded = false
        var moviesList: List<Movie>? = null

        fun getMovieByID(movieID: Int): Movie? =
            moviesList?.let {
                it.first { movie -> movie.id == movieID}
            }
    }
}