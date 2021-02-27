package com.example.academyproject.models

interface MovieDetailsLoaderSubscriber {
    fun onMovieCreditsLoaded(movieID: Int, actors: List<Actor>)
    fun onMovieLoaded(movie: Movie?)
}