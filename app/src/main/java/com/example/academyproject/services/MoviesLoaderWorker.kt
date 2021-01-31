package com.example.academyproject.services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.academyproject.caching.Cacher
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepository

class MoviesLoaderWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(applicationContext, workerParameters) {
    private val repository = MoviesRepository(applicationContext)
    private val cacher = Cacher(theMovieDbApiService, repository)

    override suspend fun doWork(): Result {
        if (cacher.loadMoviesListAndDetails())
            return Result.success()

        return Result.failure()
    }
}