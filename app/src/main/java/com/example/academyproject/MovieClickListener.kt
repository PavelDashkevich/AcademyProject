package com.example.academyproject

import com.example.academyproject.data.Movie

interface MovieClickListener {
    fun onMovieClick(movie: Movie)
}