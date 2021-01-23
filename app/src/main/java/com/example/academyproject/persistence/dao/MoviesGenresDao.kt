package com.example.academyproject.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.academyproject.persistence.entities.MoviesGenresEntity

@Dao
interface MoviesGenresDao {
    @Insert
    suspend fun insert(moviesGenres: List<MoviesGenresEntity>)

    @Query("DELETE FROM movies_genres WHERE movie_id == :movieId")
    suspend fun deleteByMovieId(movieId: Int)
}