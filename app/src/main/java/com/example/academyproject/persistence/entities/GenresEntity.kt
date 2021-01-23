package com.example.academyproject.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.academyproject.persistence.MoviesDbContract

@Entity(
    tableName = MoviesDbContract.Genres.TABLE_NAME,
    indices = [Index(MoviesDbContract.Genres.COLUMN_NAME_ID)])
data class GenresEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = MoviesDbContract.Genres.COLUMN_NAME_ID)
    val id: Int = 0,

    @ColumnInfo(name = MoviesDbContract.Genres.COLUMN_NAME_NAME)
    val name: String
)