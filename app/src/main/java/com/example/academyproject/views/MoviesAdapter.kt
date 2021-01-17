package com.example.academyproject.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academyproject.R
import com.example.academyproject.models.Movie
import com.example.academyproject.viewmodels.MoviesViewModel

class MoviesAdapter(
    var movies: MutableList<Movie>,
    private val itemClickListener: MovieClickListener,
    private val viewModel: MoviesViewModel
): RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater
                            .from(parent.context)
                            .inflate(R.layout.movie_item, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (!movies[position].runtimeLoaded)
            viewModel.loadMovieDetails(movies[position].id)

        holder.bind(movies[position], itemClickListener)
    }

    override fun getItemCount(): Int = movies.size
}