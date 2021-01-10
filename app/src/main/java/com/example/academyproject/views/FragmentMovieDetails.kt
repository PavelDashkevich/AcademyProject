package com.example.academyproject.views

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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.academyproject.R
import com.example.academyproject.models.data.Movie
import com.example.academyproject.viewmodels.MoviesViewModel

class FragmentMovieDetails: Fragment() {
    private val viewModel: MoviesViewModel by activityViewModels()
    private var movie: Movie? = null
    private var movieID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        movieID?.let { selectMovie(it) }
        viewModel.selectedMovie.observe(viewLifecycleOwner, this::updateMovieData)
        updateMovieData(viewModel.selectedMovie.value)
    }

    private fun selectMovie(movieID: Int) {
        viewModel.selectMovie(movieID)
    }

    private fun updateMovieData(newMovie: Movie?) {
        movie = newMovie
        view?.let {
            setMovieData(it)
        }
    }

    private fun setMovieData(view: View) {
        setMovieName(view)
        setMovieGenre(view)
        setMovieContentRating(view)
        setMovieStoryline(view)
        setMovieReviewsNumber(view)
        setMovieCastHeaderProperties(view)
        setMovieBackButtonProperties(view)
        setMovieRating(view)
        setMovieBackdropImage(view)
        setMovieActors(view)
    }

    private fun setMovieName(view: View) {
        val name: TextView = view.findViewById(R.id.tv_movie_name)
        name.text = movie?.title ?: ""
    }

    private fun setMovieGenre(view: View) {
        val genre: TextView = view.findViewById(R.id.tv_movie_genre)
        genre.text = movie?.let { movie -> movie.genres.joinToString { it.name } }
    }

    private fun setMovieContentRating(view: View) {
        val contentRating: TextView = view.findViewById(R.id.tv_movie_content_rating)
        contentRating.text = getString(R.string.content_rating, movie?.minimumAge ?: 13)
    }

    private fun setMovieStoryline(view: View) {
        val storyline: TextView = view.findViewById(R.id.tv_storyline_text)
        storyline.text = movie?.overview ?: ""
    }

    private fun setMovieReviewsNumber(view: View) {
        val reviewsNumber: TextView = view.findViewById(R.id.tv_reviews_number)
        val numOfRatings = movie?.numberOfRatings ?: 0
        reviewsNumber.text = view.context.resources.getQuantityString(R.plurals.reviews_num, numOfRatings, numOfRatings)
    }

    private fun setMovieCastHeaderProperties(view: View) {
        val castHeader: TextView = view.findViewById(R.id.tv_cast_header)
        castHeader.visibility = if ((movie?.actors?.size ?: 0) == 0) View.GONE else View.VISIBLE
    }

    private fun setMovieBackButtonProperties(view: View) {
        val back: TextView = view.findViewById(R.id.tv_top_menu_back)
        back.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0)
                parentFragmentManager.popBackStack()
        }
    }

    private fun setMovieRating(view: View) {
        val rating: RatingBar = view.findViewById(R.id.rb_rating_stars)
        rating.rating = (movie?.ratings ?: 0F) / 2F
    }

    private fun setMovieBackdropImage(view: View) {
        val image: ImageView = view.findViewById(R.id.iv_background)
        movie?.let { loadBackdropImage(it, image) }

    }

    private fun loadBackdropImage(movie: Movie, image: ImageView) {
        val colorMatrix = ColorMatrix()

        context?.let {
            val colorDrawable = ColorDrawable(ResourcesCompat.getColor(it.resources,
                R.color.background, null))

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

    private fun setMovieActors(view: View) {
        val recycler: RecyclerView = view.findViewById(R.id.rv_actors_list)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.addItemDecoration(ActorItemDecoration(resources.getDimension(R.dimen.actor_photo_margin_side).toInt()))
        val adapter = ActorsAdapter(movie?.actors ?: listOf())
        recycler.adapter = adapter
    }

    companion object {
        fun newInstance(idOfSelectedMovie: Int): FragmentMovieDetails {
            val fragment = FragmentMovieDetails()
            fragment.movieID = idOfSelectedMovie

            return fragment
        }
    }
}