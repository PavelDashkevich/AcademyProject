package com.example.academyproject

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyproject.data.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentMoviesList: Fragment() {
    private lateinit var adapter: MoviesAdapter
    private lateinit var movieClickListener: MovieClickListener
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var currentOrientation = Configuration.ORIENTATION_UNDEFINED
    private var coroutineScopeDefault = CoroutineScope(Dispatchers.Default)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MovieClickListener)
            movieClickListener = context
    }

    private suspend fun loadData() = withContext(Dispatchers.Main) {
        if (!DataUtil.isDataLoaded) {
            progressBar.visibility = View.VISIBLE

            context?.let {
                DataUtil.moviesList = loadMovies(it)
                DataUtil.isDataLoaded = true
            }

            progressBar.visibility = View.GONE
        }
    }

    private suspend fun updateAdapter() = withContext(Dispatchers.Main) {
        val movies = DataUtil.moviesList ?: listOf()

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
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
        recycler = view.findViewById(R.id.rv_movies_list)
        progressBar = view.findViewById(R.id.pb_content_loading)
        recycler.layoutManager = GridLayoutManager(
            requireContext(),
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
                COLUMNS_NUM_PORTRAIT
            else
                COLUMNS_NUM_LANDSCAPE
        )

        adapter = MoviesAdapter(mutableListOf(), movieClickListener)
        recycler.adapter = adapter

        coroutineScopeDefault.launch {
            loadData()
            updateAdapter()
        }
    }

    companion object {
        fun newInstance(): FragmentMoviesList {
            return FragmentMoviesList()
        }

        const val COLUMNS_NUM_PORTRAIT = 2
        const val COLUMNS_NUM_LANDSCAPE = 3
    }
}