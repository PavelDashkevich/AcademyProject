package com.example.academyproject

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class FragmentMoviesList: Fragment() {
    private lateinit var adapter: MoviesAdapter
    private lateinit var movieClickListener: MovieClickListener
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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler: RecyclerView = view.findViewById(R.id.rv_movies_list)
        val movies = DataUtil.generateData()
        recycler.layoutManager = GridLayoutManager(
                requireContext(),
                if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
                    COLUMNS_NUM_PORTRAIT
                else
                    COLUMNS_NUM_LANDSCAPE
        )
        adapter = MoviesAdapter(view.context, movies, movieClickListener)
        recycler.adapter = adapter
    }

    companion object {
        fun newInstance(): FragmentMoviesList {
            return FragmentMoviesList()
        }

        const val COLUMNS_NUM_PORTRAIT = 2
        const val COLUMNS_NUM_LANDSCAPE = 3
    }
}