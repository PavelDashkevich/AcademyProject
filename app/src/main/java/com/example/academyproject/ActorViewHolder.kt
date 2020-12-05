package com.example.academyproject

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

class ActorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val actorLayout: ConstraintLayout = itemView.findViewById(R.id.cl_actor)

    private val name: TextView = itemView.findViewById(R.id.tv_movie_actor_name)

    private val image: ShapeableImageView = itemView.findViewById(R.id.siv_movie_actor_photo)

    fun bind(actor: Actor) {
        var context = itemView.context

        name.text = actor.name
        image.setImageResource(
                context.resources.getIdentifier(
                        actor.photo,
                        "drawable",
                        context.packageName
                )
        )

        applyCornersToActorPhoto(image)
    }

    private fun applyCornersToActorPhoto(img: ShapeableImageView) {
        val rad: Float = itemView.context.resources.getDimension(R.dimen.actor_photo_corner_radius)

        img.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, rad)
                .setTopLeftCorner(CornerFamily.ROUNDED, rad)
                .setBottomRightCorner(CornerFamily.ROUNDED, rad)
                .setBottomLeftCorner(CornerFamily.ROUNDED, rad)
                .build()
    }
}