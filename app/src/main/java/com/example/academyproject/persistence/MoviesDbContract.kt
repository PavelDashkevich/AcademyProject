package com.example.academyproject.persistence

import android.provider.BaseColumns

object MoviesDbContract {
    const val DATABASE_NAME = "movies.db"

    object Actors {
        const val TABLE_NAME = "actors"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_PROFILE_PATH = "profile_path"
    }

    object Genres {
        const val TABLE_NAME = "genres"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
    }

    object Movies {
        const val TABLE_NAME = "movies"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_POSTER_PATH = "poster_path"
        const val COLUMN_NAME_BACKDROP_PATH = "backdrop_path"
        const val COLUMN_NAME_VOTE_AVERAGE = "vote_average"
        const val COLUMN_NAME_VOTE_COUNT = "vote_count"
        const val COLUMN_NAME_ADULT = "adult"
        const val COLUMN_NAME_RUNTIME = "runtime"
    }

    object MoviesActors {
        const val TABLE_NAME = "movies_actors"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_MOVIE_ID = "movie_id"
        const val COLUMN_NAME_ACTOR_ID = "actor_id"
    }

    object MoviesGenres {
        const val TABLE_NAME = "movies_genres"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_MOVIE_ID = "movie_id"
        const val COLUMN_NAME_GENRE_ID = "genre_id"
    }
}