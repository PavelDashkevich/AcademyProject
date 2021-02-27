package com.example.academyproject.services

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.academyproject.caching.Cacher
import com.example.academyproject.models.Movie
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.notifications.Notifications
import com.example.academyproject.persistence.MoviesRepositorySingleton

class MoviesLoaderWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(applicationContext, workerParameters) {
    private val repository = MoviesRepositorySingleton.getInstance(applicationContext)
    private val cacher = Cacher(theMovieDbApiService, repository)
    private val notifications = Notifications(applicationContext)

    override suspend fun doWork(): Result {
        Log.d("MovieApp", "MoviesLoaderWorker: doWork()")

        var result: Result = Result.failure()
        val topRatedMovieBeforeLoading: Movie? = repository.getTopRatedMovie()

        if (cacher.loadMoviesListAndDetails()) {
            result = Result.success()
        }

        val topRatedMovieAfterLoading: Movie? = repository.getTopRatedMovie()

        topRatedMovieAfterLoading?.let {
            if (topRatedMovieBeforeLoading != topRatedMovieAfterLoading) {
                notifications.showNotification(topRatedMovieAfterLoading)
            }
        }

        if (result == Result.failure()) {
            Log.d("MovieApp", "MoviesLoaderWorker: doWork(): failure")
        } else {
            Log.d("MovieApp", "MoviesLoaderWorker: doWork(): success")
        }
        return result
    }
}