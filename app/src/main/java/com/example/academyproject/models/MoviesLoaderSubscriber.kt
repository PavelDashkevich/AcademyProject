package com.example.academyproject.models

interface MoviesLoaderSubscriber {
    fun onMoviesLoaded(movies: List<Movie>, errorMsg: String = "")
    fun onMoviesFromFlowLoaded(movies: List<Movie>)
    fun onMovieDetailsLoaded(movie: Movie)
}