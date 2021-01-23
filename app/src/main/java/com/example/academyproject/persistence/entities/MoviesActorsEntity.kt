package com.example.academyproject.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.academyproject.persistence.MoviesDbContract

@Entity(
    tableName = MoviesDbContract.MoviesActors.TABLE_NAME,
    indices = [Index(MoviesDbContract.MoviesActors.COLUMN_NAME_ID)])
data class MoviesActorsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MoviesDbContract.MoviesActors.COLUMN_NAME_ID)
    val id: Long = 0,

    @ColumnInfo(name = MoviesDbContract.MoviesActors.COLUMN_NAME_MOVIE_ID)
    val movie_id: Int = 0,

    @ColumnInfo(name = MoviesDbContract.MoviesActors.COLUMN_NAME_ACTOR_ID)
    val actor_id: Int = 0
)