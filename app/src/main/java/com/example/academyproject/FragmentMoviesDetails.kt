package com.example.academyproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

class FragmentMoviesDetails: Fragment() {
    private fun applyCornersToActorPhoto(rad: Float, img: ShapeableImageView?) {
        img?.shapeAppearanceModel
            ?.toBuilder()
            ?.setTopRightCorner(CornerFamily.ROUNDED, rad)
            ?.setTopLeftCorner(CornerFamily.ROUNDED, rad)
            ?.setBottomRightCorner(CornerFamily.ROUNDED, rad)
            ?.setBottomLeftCorner(CornerFamily.ROUNDED, rad)
            ?.build()
    }

    private fun applyCornersToActorsPhotos(view: View) {
        val rad: Float = resources.getDimension(R.dimen.actor_photo_corner_radius)

        applyCornersToActorPhoto(rad, view.findViewById(R.id.siv_movie_actor_photo1))
        applyCornersToActorPhoto(rad, view.findViewById(R.id.siv_movie_actor_photo2))
        applyCornersToActorPhoto(rad, view.findViewById(R.id.siv_movie_actor_photo3))
        applyCornersToActorPhoto(rad, view.findViewById(R.id.siv_movie_actor_photo4))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

        view?.apply { applyCornersToActorsPhotos(this) }

        return view
    }

    companion object {
        fun newInstance(): FragmentMoviesDetails {
            return FragmentMoviesDetails()
        }
    }

}