package com.example.academyproject.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.academyproject.persistence.MoviesDbContract

@Entity(
    tableName = MoviesDbContract.Movies.TABLE_NAME,
    indices = [Index(MoviesDbContract.Movies.COLUMN_NAME_ID)])
data class MoviesEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_ID)
    val id: Int = 0,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_TITLE)
    val title: String,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_OVERVIEW)
    val overview: String,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_POSTER_PATH)
    val posterPath: String,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_BACKDROP_PATH)
    val backdropPath: String,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_VOTE_AVERAGE)
    val voteAverage: Float,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_VOTE_COUNT)
    val voteCount: Int,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_ADULT)
    val adult: Boolean,

    @ColumnInfo(name = MoviesDbContract.Movies.COLUMN_NAME_RUNTIME)
    val runtime: Int
)