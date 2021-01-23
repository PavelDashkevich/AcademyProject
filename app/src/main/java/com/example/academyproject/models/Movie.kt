package com.example.academyproject.models

import com.example.academyproject.network.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("overview")
    var overview: String,
    @SerialName("poster_path")
    var posterImagePath: String,
    @SerialName("backdrop_path")
    var backdropImagePath: String,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val numberOfRatings: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("runtime")
    var runtime: Int? = null,
    @SerialName("genre_ids")
    var genreIds: List<Int> = listOf(),
    var genres: List<Genre> = listOf(),
    var actors: List<Actor> = listOf(),
    var runtimeLoaded: Boolean = false,
    var actorsLoaded: Boolean = false
) {
    val contentRating: String
        get() = if (adult) "16+" else "PG"

    fun applyImageBaseUrl(imageBaseUrl: String) {
        posterImagePath = "$imageBaseUrl$POSTER_IMG_SIZE/$posterImagePath"
        backdropImagePath = "$imageBaseUrl$BACKDROP_IMG_SIZE/$backdropImagePath"
    }
}