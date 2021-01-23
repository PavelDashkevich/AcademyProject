package com.example.academyproject.network

import com.example.academyproject.models.Actor
import com.example.academyproject.models.Genre
import com.example.academyproject.models.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDbApiService {
    @GET("configuration")
    suspend fun getConfiguration(): ConfigurationResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): TopRatedMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieID: Int): Movie

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieID: Int): MovieCreditsResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse
}

@Serializable
class ConfigurationResponse(
    @SerialName("images")
    val images: ImagesConfiguration
)

@Serializable
class ImagesConfiguration(
    @SerialName("secure_base_url")
    val baseUrl: String
)

@Serializable
class GenresResponse(
    @SerialName("genres")
    val genres: List<Genre>
)

@Serializable
class TopRatedMoviesResponse(
    @SerialName("results")
    val results: List<Movie>
)

@Serializable
class MovieCreditsResponse(
    @SerialName("cast")
    val cast: List<Actor> = listOf()
)