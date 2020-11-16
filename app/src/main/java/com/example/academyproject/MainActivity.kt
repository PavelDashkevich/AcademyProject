package com.example.academyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

class MainActivity : AppCompatActivity() {

    private fun applyCornersToActorPhoto(rad: Float, img: ShapeableImageView?) {
        img?.shapeAppearanceModel
                ?.toBuilder()
                ?.setTopRightCorner(CornerFamily.ROUNDED, rad)
                ?.setTopLeftCorner(CornerFamily.ROUNDED, rad)
                ?.setBottomRightCorner(CornerFamily.ROUNDED, rad)
                ?.setBottomLeftCorner(CornerFamily.ROUNDED, rad)
                ?.build()
    }

    private fun applyCornersToActorsPhotos() {
        val rad: Float = resources.getDimension(R.dimen.actor_photo_corner_radius)

        applyCornersToActorPhoto(rad, findViewById(R.id.siv_movie_actor_photo1))
        applyCornersToActorPhoto(rad, findViewById(R.id.siv_movie_actor_photo2))
        applyCornersToActorPhoto(rad, findViewById(R.id.siv_movie_actor_photo3))
        applyCornersToActorPhoto(rad, findViewById(R.id.siv_movie_actor_photo4))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        applyCornersToActorsPhotos()
    }
}