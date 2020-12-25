package com.example.academyproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academyproject.data.Movie

class MoviesAdapter(
    var movies: MutableList<Movie>,
    private val itemClickListener: MovieClickListener
): RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater
                            .from(parent.context)
                            .inflate(R.layout.view_holder_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], itemClickListener)
    }

    override fun getItemCount(): Int = movies.size
}