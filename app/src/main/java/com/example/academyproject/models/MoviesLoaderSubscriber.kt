package com.example.academyproject.models

interface MoviesLoaderSubscriber {
    fun onMoviesLoaded(movies: List<Movie>, imagesBaseUrl: String, errorMsg: String = "")
    fun onMovieDetailsLoaded(movie: Movie)
    fun onMovieCreditsLoaded(movieID: Int, actors: List<Actor>)
}