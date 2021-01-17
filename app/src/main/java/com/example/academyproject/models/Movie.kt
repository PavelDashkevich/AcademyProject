package com.example.academyproject.models

import com.example.academyproject.network.BACKDROP_IMG_SIZE
import com.example.academyproject.network.JsonMovie
import com.example.academyproject.network.POSTER_IMG_SIZE

data class Movie(
    val id: Int,
    val title: String,
    var overview: String,
    var poster: String,
    var backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    var runtime: Int?,
    val genres: List<Genre>,
    var actors: List<Actor>,
    var runtimeLoaded: Boolean,
    var actorsLoaded: Boolean
) {
    constructor(jsonMovie: JsonMovie, genres: List<Genre>, actors: List<Actor>) : this(
        id = jsonMovie.id,
        title = jsonMovie.title,
        overview = jsonMovie.overview,
        poster = jsonMovie.posterPicture,
        backdrop = jsonMovie.backdropPicture,
        ratings = jsonMovie.ratings,
        numberOfRatings = jsonMovie.votesCount,
        minimumAge = if (jsonMovie.adult) 16 else 13,
        runtime = jsonMovie.runtime,
        genres = genres,
        actors = actors,
        runtimeLoaded = false,
        actorsLoaded = false
    )

    fun applyBaseUrl(baseUrl: String) {
        poster = "$baseUrl$POSTER_IMG_SIZE$poster"
        backdrop = "$baseUrl$BACKDROP_IMG_SIZE$backdrop"
    }
}