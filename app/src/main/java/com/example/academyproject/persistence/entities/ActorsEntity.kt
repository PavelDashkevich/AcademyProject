package com.example.academyproject.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.academyproject.persistence.MoviesDbContract

@Entity(
    tableName = MoviesDbContract.Actors.TABLE_NAME,
    indices = [Index(MoviesDbContract.Actors.COLUMN_NAME_ID)])
data class ActorsEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = MoviesDbContract.Actors.COLUMN_NAME_ID)
    val id: Int = 0,

    @ColumnInfo(name = MoviesDbContract.Actors.COLUMN_NAME_NAME)
    val name: String,

    @ColumnInfo(name = MoviesDbContract.Actors.COLUMN_NAME_PROFILE_PATH)
    val profilePath: String
)