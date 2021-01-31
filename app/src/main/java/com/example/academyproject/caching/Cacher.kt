package com.example.academyproject.caching

import android.util.Log
import com.example.academyproject.models.Actor
import com.example.academyproject.models.Genre
import com.example.academyproject.models.Movie
import com.example.academyproject.network.TheMovieDbApiService
import com.example.academyproject.network.imageBaseUrl
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class Cacher(
    private val movieDbApiService: TheMovieDbApiService,
    private val repository: MoviesRepository
) {
    suspend fun loadMoviesList(): List<Movie> = withContext(Dispatchers.IO) {
        val genres = movieDbApiService.getGenres().genres
        repository.replaceAllGenres(genres)

        val genresMap = genres.associateBy { it.id }
        imageBaseUrl = movieDbApiService.getConfiguration().images.baseUrl
        val movies: List<Movie> = movieDbApiService.getTopRatedMovies().results
        movies.forEach { movie ->
            movie.applyImageBaseUrl(imageBaseUrl)
            movie.genres = movie.genreIds.map {
                genresMap[it] ?: Genre(it, "Unknown")
            }
        }
        repository.insertMovies(movies)

        return@withContext movies
    }

    suspend fun loadMovieDetails(movieID: Int): Movie = withContext(Dispatchers.IO) {
        val movie = theMovieDbApiService.getMovieDetails(movieID)
        repository.updateRuntimeById(movie.runtime ?: 0, movie.id)

        return@withContext movie
    }

    suspend fun loadMovieCredits(movieID: Int): List<Actor> = withContext(Dispatchers.IO) {
        val actors = theMovieDbApiService.getMovieCredits(movieID).cast
        actors.forEach { it.applyImageBaseUrl(imageBaseUrl) }
        repository.insertOrReplaceActorsByMovieId(actors, movieID)

        return@withContext actors
    }

    suspend fun loadMoviesListAndDetails(): Boolean = withContext(Dispatchers.IO) {
        var isOk = true

        try {
            val movies = loadMoviesList()
            movies.forEach { movie ->
                val movieDetails = loadMovieDetails(movie.id)
                movie.runtime = movieDetails.runtime
                loadMovieCredits(movie.id)
            }
        } catch (e: Exception) {
            Log.d("MoviesApp", "Error occurred: $e")
            isOk = false
        }

        return@withContext isOk
    }
}