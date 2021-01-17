package com.example.academyproject.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDbApiService {
    @GET("configuration")
    suspend fun getConfiguration(): JsonConfiguration

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): TopRatedMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieID: Int): JsonMovie

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieID: Int): MovieCreditsResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): JsonGenresResponse
}

@Serializable
class JsonConfiguration(
    @SerialName("images")
    val images: JsonImagesConfiguration
)

@Serializable
class JsonImagesConfiguration(
    @SerialName("secure_base_url")
    val baseUrl: String
)

@Serializable
class JsonGenresResponse(
    @SerialName("genres")
    val genres: List<JsonGenre>
)

@Serializable
class JsonGenre(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

@Serializable
class TopRatedMoviesResponse(
    @SerialName("results")
    val results: List<JsonMovie>
)

@Serializable
class MovieCreditsResponse(
    @SerialName("cast")
    val cast: List<JsonActor> = listOf()
)

@Serializable
class JsonMovie(
    // common fields: they can be got by list of movies request
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    var posterPicture: String,
    @SerialName("backdrop_path")
    var backdropPicture: String,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val votesCount: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("genre_ids")
    val genreIds: List<Int> = listOf(),

    // fields below can be got only by movie details request
    @SerialName("runtime")
    val runtime: Int? = null
)

@Serializable
class JsonActor(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val imageUrl: String?
)