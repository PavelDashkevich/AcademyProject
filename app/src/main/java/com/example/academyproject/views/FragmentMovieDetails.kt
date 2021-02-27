package com.example.academyproject.views

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.academyproject.R
import com.example.academyproject.models.Movie
import com.example.academyproject.viewmodels.MovieDetailsViewModel
import com.example.academyproject.viewmodels.MoviesViewModelFactory

class FragmentMovieDetails: Fragment(R.layout.fragment_movies_details) {
    private val viewModel: MovieDetailsViewModel by viewModels {
        MoviesViewModelFactory(requireContext().applicationContext)
    }
    private var movie: Movie? = null
    private var movieId: Int? = null

    private lateinit var name: TextView
    private lateinit var genre: TextView
    private lateinit var contentRating: TextView
    private lateinit var storyline: TextView
    private lateinit var reviewsNumber: TextView
    private lateinit var back: TextView
    private lateinit var rating: RatingBar
    private lateinit var image: ImageView
    private lateinit var castHeader: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var progressBarActors: ProgressBar
    private lateinit var progressBarMovie: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewElements(view)
        viewModel.actorsUpdatedInMovie.observe(viewLifecycleOwner, this::refreshActors)
        viewModel.movieGotById.observe(viewLifecycleOwner, this::refreshContent)
        selectMovie()
        setMovieData(view, viewModel.movieGotById.value ?: false)
    }

    private fun selectMovie() {
        if (movie == null && movieId == null) { // configuration change
            movie = viewModel.getSelectedMovie()
        } else {
            if (movieId != null) { // select movie by Id (user opens movie from deep link)
                movieId?.let { viewModel.selectMovieById(it) }
            } else { // select movie by ref (user opens movie from FragmentMoviesList)
                movie?.let { viewModel.selectMovie(it) }
            }
        }
    }

    private fun refreshActors(actorsLoaded: Boolean) {
        if (actorsLoaded) updateRecyclerView()
    }

    private fun refreshContent(contentLoaded: Boolean) {
        Log.d("MovieApp", "FragmentMovieDetails: refreshContent($contentLoaded)")
        movie = viewModel.getSelectedMovie()
        view?.let { setMovieData(it, contentLoaded) }
    }

    private fun setMovieData(view: View, contentLoaded: Boolean) {
        val views = listOf(
            name, genre, contentRating, storyline, reviewsNumber, rating, image, castHeader,
            recycler
        )

        if (!contentLoaded) {
            progressBarMovie.visibility = View.VISIBLE
            setVisibilityToViews(views, View.GONE)
        } else {
            progressBarMovie.visibility = View.GONE
            setVisibilityToViews(views, View.VISIBLE)

            val numOfRatings = movie?.numberOfRatings ?: 0

            name.text = movie?.title ?: ""
            genre.text = movie?.let { movie -> movie.genres.joinToString { it.name } }
            contentRating.text = movie?.contentRating ?: "PG"
            storyline.text = movie?.overview ?: ""
            reviewsNumber.text = view.context.resources.getQuantityString(
                R.plurals.reviews_num, numOfRatings, numOfRatings
            )
            rating.rating = (movie?.ratings ?: 0F) / 2F
            movie?.let { loadBackdropImage(it) }
            setMovieActorsBlock()
        }

        back.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0)
                parentFragmentManager.popBackStack()
        }
    }

    private fun setVisibilityToViews(views: List<View>, visibility: Int) {
        views.forEach { it.visibility = visibility }
    }

    private fun setupViewElements(view: View) {
        name = view.findViewById(R.id.tv_movie_name)
        genre = view.findViewById(R.id.tv_movie_genre)
        contentRating = view.findViewById(R.id.tv_movie_content_rating)
        storyline = view.findViewById(R.id.tv_storyline_text)
        reviewsNumber = view.findViewById(R.id.tv_reviews_number)
        back = view.findViewById(R.id.tv_top_menu_back)
        rating = view.findViewById(R.id.rb_rating_stars)
        image = view.findViewById(R.id.iv_background)
        castHeader = view.findViewById(R.id.tv_cast_header)
        recycler = view.findViewById(R.id.rv_actors_list)
        progressBarActors = view.findViewById(R.id.pb_loading_actors)
        progressBarMovie = view.findViewById(R.id.pb_loading_movie)
    }

    private fun loadBackdropImage(movie: Movie) {
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
                .load(movie.backdropImagePath)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        }

        colorMatrix.setSaturation(0F)
        image.colorFilter = ColorMatrixColorFilter(colorMatrix)

        image.contentDescription = movie.title
    }

    private fun setMovieActorsBlock() {
        var progressBarVisibility = View.GONE

        movie?.let {
            if (it.actorsLoaded) {
                updateRecyclerView()
                return
            } else {
                progressBarVisibility = View.VISIBLE
            }
        }

        progressBarActors.visibility = progressBarVisibility
        castHeader.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    private fun updateRecyclerView() {
        val adapter = ActorsAdapter(movie?.actors ?: listOf())

        progressBarActors.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        castHeader.visibility = if ((movie?.actors?.size ?: 0) == 0) View.GONE else View.VISIBLE

        recycler.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recycler.addItemDecoration(ActorItemDecoration(resources.getDimension(R.dimen.actor_photo_margin_side).toInt()))
        recycler.adapter = adapter
    }

    companion object {
        fun newInstance(movie: Movie): FragmentMovieDetails {
            val fragment = FragmentMovieDetails()
            fragment.movie = movie

            return fragment
        }

        fun newInstance(movieId: Int): FragmentMovieDetails {
            val fragment = FragmentMovieDetails()
            fragment.movieId = movieId

            return fragment
        }
    }
}