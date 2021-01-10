package com.example.academyproject.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.academyproject.R
import com.example.academyproject.models.data.Movie

class MainActivity : AppCompatActivity(), MovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fl_main, FragmentMoviesList.newInstance())
                commit()
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.fl_main, FragmentMovieDetails.newInstance(movie.id))
            commit()
        }
    }
}

interface MovieClickListener {
    fun onMovieClick(movie: Movie)
}