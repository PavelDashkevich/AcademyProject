package com.example.academyproject.services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.academyproject.caching.Cacher
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepositorySingleton

class MoviesLoaderWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(applicationContext, workerParameters) {
    private val repository = MoviesRepositorySingleton.getInstance(applicationContext)
    private val cacher = Cacher(theMovieDbApiService, repository)

    override suspend fun doWork(): Result {
        Log.d("MovieApp", "MoviesLoaderWorker: doWork()")
        if (cacher.loadMoviesListAndDetails()) {
            Log.d("MovieApp", "MoviesLoaderWorker: doWork(): success")
            return Result.success()
        }

        Log.d("MovieApp", "MoviesLoaderWorker: doWork(): failure")
        return Result.failure()
    }
}