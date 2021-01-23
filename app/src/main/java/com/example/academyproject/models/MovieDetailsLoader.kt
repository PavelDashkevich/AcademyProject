package com.example.academyproject.models

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
    fun requestMovieCredits(movieID: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var actors = repository.getActorsByMovieId(movieID)

            if (actors.isNotEmpty())
                handler?.onMovieCreditsLoaded(movieID, actors)

            try {
                actors = theMovieDbApiService.getMovieCredits(movieID).cast
                actors.forEach { it.applyImageBaseUrl(imageBaseUrl) }
                repository.insertOrReplaceActorsByMovieId(actors, movieID)

                handler?.onMovieCreditsLoaded(movieID, actors)
            } catch (e: Exception) {

            }
        }
    }
}