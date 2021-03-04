package com.example.academyproject.views

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.net.Uri
import android.os.BaseBundle
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import java.time.LocalDate

class FragmentMovieDetails:
    Fragment(R.layout.fragment_movies_details),
    CalendarPermissionHelper.PermissionGrantedListener {
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
    private lateinit var scheduleViewing: Button

    private val calendarPermissionHelper = CalendarPermissionHelper(this, this)

    // region implements Fragment lifecycle events
    @SuppressLint("MissingPermission")
    override fun onAttach(context: Context) {
        super.onAttach(context)

        calendarPermissionHelper.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(FragmentDatePicker.KEY_RESULT, ::onDateSet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewElements(view)
        viewModel.actorsUpdatedInMovie.observe(viewLifecycleOwner, this::refreshActors)
        viewModel.movieGotById.observe(viewLifecycleOwner, this::refreshContent)
        selectMovie()
        setMovieData(view, viewModel.movieGotById.value ?: false)

        scheduleViewing.setOnClickListener { calendarPermissionHelper.requestPermission() }
    }

    override fun onDetach() {
        calendarPermissionHelper.terminate()

        super.onDetach()
    }
    // endregion implements Fragment lifecycle events

    // region setup views from movie
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
            recycler, scheduleViewing
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
        scheduleViewing = view.findViewById(R.id.btn_schedule_viewing)
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
    // endregion setup views from movie

    // region schedule movie viewing to calendar (with permissions request)
    override fun onCalendarPermissionGranted() {
        val dialog = FragmentDatePicker()
        dialog.show(requireActivity().supportFragmentManager, "schedule")
    }

    private fun getCalendarId(): Long? {
        var result: Long? = null

        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )

        var cursor = requireContext().contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            "${CalendarContract.Calendars.IS_PRIMARY} = 1",
            null,
            "${CalendarContract.Calendars._ID} ASC"
        )

        if (cursor != null && cursor.count <= 0) {
            cursor = requireContext().contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                "${CalendarContract.Calendars.VISIBLE} = 1",
                null,
                "${CalendarContract.Calendars._ID} ASC"
            )
        }

        cursor?.let {
            if (it.moveToFirst()) {
                result = it.getString(it.getColumnIndex(projection[0])).toLong()
            }

            it.close()
        }

        return result
    }

    @SuppressLint("NewApi")
    fun onDateSet(key: String, bundle: BaseBundle) {
        val now = LocalDate.now()
        val year = bundle.getInt(FragmentDatePicker.BUNDLE_KEY_YEAR, now.year)
        val month = bundle.getInt(FragmentDatePicker.BUNDLE_KEY_MONTH, now.monthValue)
        val dayOfMonth = bundle.getInt(FragmentDatePicker.BUNDLE_KEY_DAY_OF_MONTH, now.dayOfMonth)

        movie?.let {
            val calendarId = getCalendarId()

            if (calendarId == null) {
                Toast.makeText(context, "Calendar not found.", Toast.LENGTH_SHORT).show()
            } else {
                val dateStart: Long = Calendar.getInstance().run {
                    set(year, month, dayOfMonth)
                    timeInMillis
                }

                val values = ContentValues().apply {
                    put(CalendarContract.Events.DTSTART, dateStart)
                    put(CalendarContract.Events.DTEND, dateStart)
                    put(CalendarContract.Events.TITLE, "Don't forget to see this movie")
                    put(CalendarContract.Events.DESCRIPTION, it.title)
                    put(CalendarContract.Events.CALENDAR_ID, calendarId)
                    put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE)
                }

                val uri: Uri? = requireContext().contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

                uri?.let {
                    Toast.makeText(context, "Movie was added to calendar.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    // endregion schedule movie viewing to calendar (with permissions request)

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