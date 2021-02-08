package com.example.academyproject.persistence

import android.content.Context

object MoviesRepositorySingleton {
    private var repository: MoviesRepository? = null

    fun getInstance(applicationContext: Context): MoviesRepository {
        if (repository == null) {
            repository = MoviesRepository(applicationContext)
        }

        return repository as MoviesRepository
    }
}