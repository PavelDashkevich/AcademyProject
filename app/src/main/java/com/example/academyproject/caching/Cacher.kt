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
        Log.d("MovieApp", "Cacher: loadMoviesList(): movieDbApiService.getGenres() called")
        repository.replaceAllGenres(genres)
        Log.d("MovieApp", "Cacher: loadMoviesList(): repository.replaceAllGenres(genres) called")

        val genresMap = genres.associateBy { it.id }
        imageBaseUrl = movieDbApiService.getConfiguration().images.baseUrl
        Log.d("MovieApp", "Cacher: loadMoviesList(): movieDbApiService.getConfiguration() called")
        val movies: List<Movie> = movieDbApiService.getTopRatedMovies().results
        Log.d("MovieApp", "Cacher: loadMoviesList(): movieDbApiService.getTopRatedMovies() called")
        movies.forEach { movie ->
            movie.applyImageBaseUrl(imageBaseUrl)
            movie.genres = movie.genreIds.map {
                genresMap[it] ?: Genre(it, "Unknown")
            }
        }
        repository.insertMovies(movies)
        Log.d("MovieApp", "Cacher: loadMoviesList(): repository.insertMovies(movies) called")

        return@withContext movies
    }

    suspend fun loadMovieDetails(movieID: Int): Movie = withContext(Dispatchers.IO) {
        val movie = theMovieDbApiService.getMovieDetails(movieID)
        Log.d("MovieApp", "Cacher: loadMovieDetails(${movieID}): theMovieDbApiService.getMovieDetails() called")
        repository.updateRuntimeById(movie.runtime ?: 0, movie.id)
        Log.d("MovieApp", "Cacher: loadMovieDetails(${movieID}): repository.updateRuntimeById called")

        return@withContext movie
    }

    suspend fun loadMovieCredits(movieID: Int): List<Actor> = withContext(Dispatchers.IO) {
        val actors = theMovieDbApiService.getMovieCredits(movieID).cast
        Log.d("MovieApp", "Cacher: loadMovieCredits(${movieID}): theMovieDbApiService.getMovieCredits() called")
        actors.forEach { it.applyImageBaseUrl(imageBaseUrl) }
        repository.insertOrReplaceActorsByMovieId(actors, movieID)
        Log.d("MovieApp", "Cacher: loadMovieCredits(${movieID}): repository.insertOrReplaceActorsByMovieId called")

        return@withContext actors
    }

    suspend fun loadMoviesListAndDetails(): Boolean = withContext(Dispatchers.IO) {
        var isOk = true

        try {
            val movies = loadMoviesList()
            Log.d("MovieApp", "Cacher: loadMoviesListAndDetails(): loadMoviesList() called")
            movies.forEach { movie ->
                val movieDetails = loadMovieDetails(movie.id)
                Log.d("MovieApp", "Cacher: loadMoviesListAndDetails(): loadMovieDetails(${movie.id}) called")
                movie.runtime = movieDetails.runtime
                loadMovieCredits(movie.id)
                Log.d("MovieApp", "Cacher: loadMoviesListAndDetails(): loadMovieCredits(${movie.id}) called")
            }
        } catch (e: Exception) {
            Log.d("MoviesApp", "Cacher: loadMoviesListAndDetails(): Error occurred: $e")
            isOk = false
        }

        return@withContext isOk
    }
}