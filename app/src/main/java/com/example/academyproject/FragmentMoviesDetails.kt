package com.example.academyproject

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.academyproject.data.Movie

class FragmentMoviesDetails: Fragment() {
    private lateinit var adapter: ActorsAdapter

    private var movie: Movie? = null
    private var movieID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

        savedInstanceState?.apply {
            movieID = this.getInt(ARG_MOVIE_ID)
            movie = DataUtil.getMovieByID(movieID)
        }

        view?.apply {
            applyMovieData(this)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ARG_MOVIE_ID, movieID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler: RecyclerView = view.findViewById(R.id.rv_actors_list)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.addItemDecoration(ActorItemDecoration(resources.getDimension(R.dimen.actor_photo_margin_side).toInt()))
        adapter = ActorsAdapter(movie?.actors ?: listOf())
        recycler.adapter = adapter
    }

    private fun applyMovieData(view: View) {
        val name: TextView = view.findViewById(R.id.tv_movie_name)
        val genre: TextView = view.findViewById(R.id.tv_movie_genre)
        val contentRating: TextView = view.findViewById(R.id.tv_movie_content_rating)
        val storyline: TextView = view.findViewById(R.id.tv_storyline_text)
        val reviewsNumber: TextView = view.findViewById(R.id.tv_reviews_number)
        val back: TextView = view.findViewById(R.id.tv_top_menu_back)
        val castHeader: TextView = view.findViewById(R.id.tv_cast_header)

        val rating: RatingBar = view.findViewById(R.id.rb_rating_stars)

        val image: ImageView = view.findViewById(R.id.iv_background)

        val numOfRatings = movie?.numberOfRatings ?: 0

        name.text = movie?.title ?: ""
        genre.text = movie?.let { movie -> movie.genres.joinToString { it.name } }
        contentRating.text = getString(R.string.content_rating, movie?.minimumAge ?: 13)
        storyline.text = movie?.overview ?: ""
        reviewsNumber.text = view.context.resources.getQuantityString(R.plurals.reviews_num, numOfRatings, numOfRatings)

        castHeader.visibility = if ((movie?.actors?.size ?: 0) == 0) View.GONE else View.VISIBLE

        back.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0)
                parentFragmentManager.popBackStack()
        }

        rating.rating = (movie?.ratings ?: 0F) / 2F

        movie?.let { loadBackdropImage(it, image) }
    }

    private fun loadBackdropImage(movie: Movie, image: ImageView) {
        val colorMatrix = ColorMatrix()

        context?.let {
            val colorDrawable = ColorDrawable(ResourcesCompat.getColor(it.resources, R.color.background, null))

            val requestOptions = RequestOptions()
                .placeholder(colorDrawable)
                .error(colorDrawable)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

            Glide
                .with(it)
                .load(movie.backdrop)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        }

        colorMatrix.setSaturation(0F)
        image.colorFilter = ColorMatrixColorFilter(colorMatrix)

        image.contentDescription = movie.title
    }

    companion object {
        fun newInstance(idOfSelectedMovie: Int): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            fragment.movieID = idOfSelectedMovie
            fragment.movie = DataUtil.getMovieByID(idOfSelectedMovie)

            return fragment
        }

        const val ARG_MOVIE_ID = "MOVIE_ID"
    }
}