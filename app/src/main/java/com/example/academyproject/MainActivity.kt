package com.example.academyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.academyproject.data.Movie

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
            add(R.id.fl_main, FragmentMoviesDetails.newInstance(movie.id))
            commit()
        }
    }
}