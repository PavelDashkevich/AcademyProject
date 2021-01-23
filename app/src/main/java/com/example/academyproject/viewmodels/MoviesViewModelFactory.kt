package com.example.academyproject.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academyproject.persistence.MoviesRepository

class MoviesViewModelFactory(
    private val applicationContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            MoviesViewModel::class.java ->
                MoviesViewModel(MoviesRepository(applicationContext))
            MovieDetailsViewModel::class.java ->
                MovieDetailsViewModel(MoviesRepository(applicationContext))
            else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
        } as T
}