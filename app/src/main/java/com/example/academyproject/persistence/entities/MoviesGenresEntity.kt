package com.example.academyproject.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.academyproject.persistence.MoviesDbContract

@Entity(
    tableName = MoviesDbContract.MoviesGenres.TABLE_NAME,
    indices = [Index(MoviesDbContract.MoviesGenres.COLUMN_NAME_ID)])
data class MoviesGenresEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MoviesDbContract.MoviesGenres.COLUMN_NAME_ID)
    val id: Long = 0,

    @ColumnInfo(name = MoviesDbContract.MoviesGenres.COLUMN_NAME_MOVIE_ID)
    val movie_id: Int = 0,

    @ColumnInfo(name = MoviesDbContract.MoviesGenres.COLUMN_NAME_GENRE_ID)
    val genre_id: Int = 0
)