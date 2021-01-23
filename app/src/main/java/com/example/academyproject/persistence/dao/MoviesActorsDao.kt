package com.example.academyproject.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.academyproject.persistence.entities.MoviesActorsEntity

@Dao
interface MoviesActorsDao {
    @Insert
    suspend fun insert(moviesActors: List<MoviesActorsEntity>)

    @Query("DELETE FROM movies_actors WHERE movie_id == :movieId")
    suspend fun deleteByMovieId(movieId: Int)
}