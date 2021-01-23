package com.example.academyproject.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.academyproject.persistence.entities.GenresEntity

@Dao
interface GenresDao {
    @Insert
    suspend fun insert(genres: List<GenresEntity>)

    @Query("""SELECT * FROM genres WHERE genres._id IN (
            SELECT movies_genres.genre_id FROM movies_genres WHERE movies_genres.movie_id = :movieId 
        )""")
    suspend fun getByMovieId(movieId: Int): List<GenresEntity>

    @Query("DELETE FROM genres")
    suspend fun deleteAll()
}