package com.example.academyproject.models

import com.example.academyproject.caching.Cacher
import com.example.academyproject.network.imageBaseUrl
import com.example.academyproject.network.theMovieDbApiService
import com.example.academyproject.persistence.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsLoader(
    private var handler: MovieDetailsLoaderSubscriber? = null,
    private val repository: MoviesRepository
) {
    private val cacher = Cacher(theMovieDbApiService, repository)

    fun requestMovieCredits(movieID: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var actors = repository.getActorsByMovieId(movieID)

            if (actors.isNotEmpty())
                handler?.onMovieCreditsLoaded(movieID, actors)

            try {
                actors = cacher.loadMovieCredits(movieID)
                handler?.onMovieCreditsLoaded(movieID, actors)
            } catch (e: Exception) {

            }
        }
    }
}