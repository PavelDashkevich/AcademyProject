package com.example.academyproject.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.academyproject.persistence.entities.ActorsEntity

@Dao
interface ActorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(movies: List<ActorsEntity>)

    @Query("""SELECT * FROM actors WHERE actors._id IN (
            SELECT movies_actors.actor_id FROM movies_actors WHERE movies_actors.movie_id = :movieId 
        )""")
    suspend fun getByMovieId(movieId: Int): List<ActorsEntity>
}