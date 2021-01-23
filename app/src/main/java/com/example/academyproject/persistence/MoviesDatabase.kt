package com.example.academyproject.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.academyproject.persistence.dao.*
import com.example.academyproject.persistence.entities.*

@Database(
    entities = [
        ActorsEntity::class,
        GenresEntity::class,
        MoviesEntity::class,
        MoviesActorsEntity::class,
        MoviesGenresEntity::class
               ],
    version = 1
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val actorsDao: ActorsDao
    abstract val genresDao: GenresDao
    abstract val moviesDao: MoviesDao
    abstract val moviesActorsDao: MoviesActorsDao
    abstract val moviesGenresDao: MoviesGenresDao

    companion object {
        fun create(applicationContext: Context): MoviesDatabase = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java,
            MoviesDbContract.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}