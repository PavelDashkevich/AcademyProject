package com.example.academyproject.models

interface MoviesLoaderSubscriber {
    fun onMoviesLoaded(movies: List<Movie>, errorMsg: String = "")
    fun onMovieDetailsLoaded(movie: Movie)
}