package com.example.academyproject

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.tv_movie_name)
    private val genre: TextView = itemView.findViewById(R.id.tv_movie_genre)
    private val contentRating: TextView = itemView.findViewById(R.id.tv_movie_content_rating)
    private val duration: TextView = itemView.findViewById(R.id.tv_movie_duration)
    private val reviewsNumber: TextView = itemView.findViewById(R.id.tv_reviews_number)

    private val rating: RatingBar = itemView.findViewById(R.id.rb_rating_stars)

    private val image: ImageView = itemView.findViewById(R.id.iv_movie_image)
    private val like: ImageView = itemView.findViewById(R.id.iv_movie_like)

    fun bind(movie: Movie, clickListener: MovieClickListener) {
        var context = itemView.context

        name.text = movie.name
        genre.text = movie.genre
        contentRating.text = movie.contentRating
        duration.text = "${movie.duration} min"
        reviewsNumber.text = movie.reviewsNumber.toString() + " REVIEWS"

        rating.rating = movie.rating

        image.setImageResource(
                context.resources.getIdentifier(
                        movie.imageInList,
                        "drawable",
                        context.packageName
                )
        )
        like.setImageResource(
                context.resources.getIdentifier(
                        if (movie.like) "ic_like_on" else "ic_like_off",
                        "drawable",
                        context.packageName
                )
        )

        itemView.setOnClickListener {
            clickListener.onMovieClick(movie)
        }
    }
}