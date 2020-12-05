package com.example.academyproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

class FragmentMoviesDetails(): Fragment() {
    private lateinit var adapter: ActorsAdapter
    private lateinit var movie: Movie
    private var movieIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

        savedInstanceState?.apply {
            movieIndex = this.getInt("MOVIE_INDEX")
            movie = DataUtil.movies[movieIndex]
        }

        view?.apply {
            applyMovieDate(this)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("MOVIE_INDEX", movieIndex)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler: RecyclerView = view.findViewById(R.id.rv_actors_list)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = ActorsAdapter(view.context, movie.actors)
        recycler.adapter = adapter
    }

    private fun applyMovieDate(view: View) {
        val name: TextView = view.findViewById(R.id.tv_movie_name)
        val genre: TextView = view.findViewById(R.id.tv_movie_genre)
        val contentRating: TextView = view.findViewById(R.id.tv_movie_content_rating)
        val storyline: TextView = view.findViewById(R.id.tv_storyline_text)
        val reviewsNumber: TextView = view.findViewById(R.id.tv_reviews_number)
        val back: TextView = view.findViewById(R.id.tv_top_menu_back)

        val rating: RatingBar = view.findViewById(R.id.rb_rating_stars)

        val image: ImageView = view.findViewById(R.id.iv_background)

        name.text = movie.name
        genre.text = movie.genre
        contentRating.text = movie.contentRating
        storyline.text = movie.storyline
        reviewsNumber.text = movie.reviewsNumber.toString() + " REVIEWS"

        back.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0)
                parentFragmentManager.popBackStack()
        }

        rating.rating = movie.rating

        context?.apply {
            image.setImageResource(
                    this.resources.getIdentifier(
                            movie.imageInDetails,
                            "drawable",
                            this.packageName
                    )
            )
        }
    }

    companion object {
        fun newInstance(indexOfSelectedMovie: Int): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            fragment.movieIndex = indexOfSelectedMovie
            fragment.movie = DataUtil.movies[indexOfSelectedMovie]

            return fragment
        }
    }
}