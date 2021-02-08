package com.example.academyproject.views

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyproject.R
import com.example.academyproject.models.Movie
import com.example.academyproject.viewmodels.MoviesViewModel
import com.example.academyproject.viewmodels.MoviesViewModelFactory

class FragmentMoviesList: Fragment() {
    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(requireContext().applicationContext)
    }
    private lateinit var adapter: MoviesAdapter
    private lateinit var movieClickListener: MovieClickListener
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var currentOrientation = Configuration.ORIENTATION_UNDEFINED

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MovieClickListener)
            movieClickListener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentOrientation = resources.configuration.orientation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewElements(view)
        setListOfMovies()

        viewModel.loadMoviesFromFlow()

        viewModel.isMoviesLoading.observe(viewLifecycleOwner, this::updateLoadingProgress)
        viewModel.moviesList.observe(viewLifecycleOwner, this::updateListOfMovies)
        viewModel.latestUpdatedItemIndex.observe(viewLifecycleOwner, this::updateMovieItem)
        viewModel.errorOnMoviesLoading.observe(viewLifecycleOwner, this::showError)

        viewModel.loadMovies()

        // debug
        val topHeader: TextView? = view.findViewById(R.id.tv_top_menu_header)

        topHeader?.let {
            it.setOnClickListener {
                Log.d("MovieApp", "FragmentMoviesList: topHeader.onClickListener()")
                viewModel.loadMoviesAndDetails()
            }
        }
    }

    private fun setupViewElements(view: View) {
        progressBar = view.findViewById(R.id.pb_content_loading)
        recycler = view.findViewById(R.id.rv_movies_list)
    }

    private fun setListOfMovies() {
        recycler.layoutManager = GridLayoutManager(
            requireContext(),
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
                COLUMNS_NUM_PORTRAIT
            else
                COLUMNS_NUM_LANDSCAPE
        )

        adapter = MoviesAdapter(
            viewModel.moviesList.value as MutableList<Movie>,
            movieClickListener,
            viewModel
        )
        recycler.adapter = adapter
    }

    private fun updateLoadingProgress(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateListOfMovies(newMoviesList: List<Movie>) {
        adapter.movies.clear()
        adapter.movies.addAll(newMoviesList)
        adapter.notifyDataSetChanged()
    }

    private fun updateMovieItem(indexOfMovie: Int?) {
        indexOfMovie?.let { adapter.notifyItemChanged(it) }
    }

    private fun showError(errorMsg: String) {
        if (errorMsg != "")
            Toast.makeText(this.activity, errorMsg, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(): FragmentMoviesList {
            return FragmentMoviesList()
        }

        const val COLUMNS_NUM_PORTRAIT = 2
        const val COLUMNS_NUM_LANDSCAPE = 3
    }
}