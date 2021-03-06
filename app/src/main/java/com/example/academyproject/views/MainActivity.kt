package com.example.academyproject.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.academyproject.R
import com.example.academyproject.models.Movie
import com.example.academyproject.services.MoviesLoaderWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fl_main, FragmentMoviesList.newInstance())
                commit()
            }
            intent?.let(::handleIntent)
        }

        startWorker()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let(::handleIntent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val movieId = intent.data?.lastPathSegment?.toIntOrNull()
                movieId?.let { openMovie(it) }
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        openMovie(movie)
    }

    private fun openMovie(movie: Movie) {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.fl_main, FragmentMovieDetails.newInstance(movie))
            commit()
        }
    }

    private fun openMovie(movieId: Int) {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.fl_main, FragmentMovieDetails.newInstance(movieId))
            commit()
        }
    }
    
    private fun startWorker() {
        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = PeriodicWorkRequestBuilder<MoviesLoaderWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.METERED)
                    .setRequiresCharging(true)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            "MovieLoader",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

interface MovieClickListener {
    fun onMovieClick(movie: Movie)
}