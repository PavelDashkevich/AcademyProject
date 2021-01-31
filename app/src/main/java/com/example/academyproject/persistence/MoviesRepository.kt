package com.example.academyproject.persistence

import android.content.Context
import com.example.academyproject.models.Actor
import com.example.academyproject.models.Genre
import com.example.academyproject.models.Movie
import com.example.academyproject.persistence.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MoviesRepository(applicationContext: Context) {
    private val moviesDb = MoviesDatabase.create(applicationContext)

    suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        return@withContext fillGenresActors(moviesDb.moviesDao.getAll())
    }

    suspend fun getAllMoviesAsFlow(): Flow<List<Movie>> = withContext(Dispatchers.IO) {
        return@withContext moviesDb.moviesDao.getAllAsFlow().map { fillGenresActors(it) }
    }

    private suspend fun fillGenresActors(moviesEntities: List<MoviesEntity>): List<Movie> {
        val movies: List<Movie> = moviesEntities.map { toMovie(it) }

        movies.forEach { movie ->
            movie.actors = moviesDb.actorsDao.getByMovieId(movie.id).map { toActor(it) }
            movie.genres = moviesDb.genresDao.getByMovieId(movie.id).map { toGenre(it) }
        }

        return movies
    }

    suspend fun replaceAllGenres(genres: List<Genre>) = withContext(Dispatchers.IO) {
        moviesDb.genresDao.deleteAll()
        moviesDb.genresDao.insert(genres.map { toGenresEntity(it) })
    }

    suspend fun insertMovies(movies: List<Movie>) = withContext(Dispatchers.IO) {
        val moviesEntityList = movies.map { toMoviesEntity(it) }
        moviesDb.moviesDao.insertOrUpdate(moviesEntityList)
        movies.forEach { movie ->
            moviesDb.moviesGenresDao.deleteByMovieId(movie.id)
            moviesDb.moviesGenresDao.insert(movie.genres.map {
                toMoviesGenresEntity(it, movie.id)
            })
        }
    }

    suspend fun getActorsByMovieId(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
        return@withContext moviesDb.actorsDao.getByMovieId(movieId).map { toActor(it) }
    }

    suspend fun insertOrReplaceActorsByMovieId(actors: List<Actor>, movieId: Int) = withContext(Dispatchers.IO) {
        moviesDb.actorsDao.insertOrUpdate(actors.map { toActorsEntity(it) })
        moviesDb.moviesActorsDao.deleteByMovieId(movieId)
        moviesDb.moviesActorsDao.insert(actors.map { toMoviesActorsEntity(it, movieId) })
    }

    suspend fun updateRuntimeById(runtime: Int, movieId: Int) = withContext(Dispatchers.IO) {
        moviesDb.moviesDao.updateRuntimeById(runtime, movieId)
    }

    // to...Entity functions
    private fun toActorsEntity(actor: Actor) = ActorsEntity(
        id = actor.id,
        name = actor.name,
        profilePath = actor.imagePath ?: ""
    )

    private fun toGenresEntity(genre: Genre) = GenresEntity(
        id = genre.id,
        name = genre.name
    )

    private fun toMoviesEntity(movie: Movie) = MoviesEntity(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        posterPath = movie.posterImagePath,
        backdropPath = movie.backdropImagePath,
        voteAverage = movie.ratings,
        voteCount = movie.numberOfRatings,
        adult = movie.adult,
        runtime = movie.runtime ?: 0
    )

    private fun toMoviesActorsEntity(actor: Actor, movieId: Int) = MoviesActorsEntity(
        actor_id = actor.id,
        movie_id = movieId
    )

    private fun toMoviesGenresEntity(genre: Genre, movieId: Int) = MoviesGenresEntity(
        genre_id = genre.id,
        movie_id = movieId
    )

    // other to... functions
    private fun toActor(entity: ActorsEntity): Actor = Actor(
        id = entity.id,
        name = entity.name,
        imagePath = entity.profilePath
    )

    private fun toGenre(entity: GenresEntity): Genre = Genre(
        id = entity.id,
        name = entity.name
    )

    private fun toMovie(entity: MoviesEntity): Movie = Movie(
        id = entity.id,
        title = entity.title,
        overview = entity.overview,
        posterImagePath = entity.posterPath,
        backdropImagePath = entity.backdropPath,
        ratings = entity.voteAverage,
        numberOfRatings = entity.voteCount,
        adult = entity.adult,
        runtime = entity.runtime,
        runtimeLoaded = true
    )
}