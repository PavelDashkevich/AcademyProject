package com.example.academyproject.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.academyproject.persistence.entities.MoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(movies: List<MoviesEntity>)

    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<MoviesEntity>

    @Query("SELECT * FROM movies")
    fun getAllAsFlow(): Flow<List<MoviesEntity>>

    @Query("UPDATE movies SET runtime = :runtime WHERE _id == :movieId")
    suspend fun updateRuntimeById(runtime: Int, movieId: Int)
}