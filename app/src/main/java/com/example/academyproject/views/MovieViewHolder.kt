package com.example.academyproject.views

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.example.academyproject.R
import com.example.academyproject.models.Movie

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
        val context = itemView.context

        name.text = movie.title
        genre.text = movie.genres.joinToString { it.name }
        contentRating.text = movie.contentRating
        duration.text =
            if (movie.runtime == null)
                ""
            else
                context.getString(R.string.movie_duration, movie.runtime ?: 0)
        reviewsNumber.text = context.resources.getQuantityString(R.plurals.reviews_num, movie.numberOfRatings, movie.numberOfRatings)

        rating.rating = movie.ratings / 2F

        loadPosterImage(movie, context)

        like.setImageResource(
                context.resources.getIdentifier(
                        /*if (movie.like) "ic_like_on" else*/ "ic_like_off",
                        "drawable",
                        context.packageName
                )
        )

        itemView.setOnClickListener {
            clickListener.onMovieClick(movie)
        }
    }

    private fun loadPosterImage(movie: Movie, context: Context) {
        val colorDrawable = ColorDrawable(ResourcesCompat.getColor(context.resources,
            R.color.background, null))

        val requestOptions = RequestOptions()
            .placeholder(colorDrawable)
            .error(
                itemView.resources.getIdentifier(
                    "pic_no_poster",
                    "drawable",
                    context.packageName
                )
            )
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        Glide
            .with(context)
            .load(
                if (movie.posterImagePath == "") {
                    itemView.resources.getIdentifier(
                        "pic_no_poster",
                        "drawable",
                        context.packageName
                    )
                } else {
                    movie.posterImagePath
                }
            )
            .onlyRetrieveFromCache(false)
            .apply(requestOptions)
            .transition(withCrossFade())
            .into(image)

        image.contentDescription = movie.title
    }
}