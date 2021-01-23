package com.example.academyproject.views

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.academyproject.R
import com.example.academyproject.models.Actor
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

class ActorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.tv_movie_actor_name)
    private val image: ShapeableImageView = itemView.findViewById(R.id.siv_movie_actor_photo)

    fun bind(actor: Actor) {
        name.text = actor.name
        image.contentDescription = actor.name
        loadActorsPhoto(actor)
    }

    private fun loadActorsPhoto(actor: Actor) {
        val context = itemView.context
        val colorDrawable = ColorDrawable(ResourcesCompat.getColor(context.resources,
            R.color.background, null))
        val requestOptions = RequestOptions()
            .placeholder(colorDrawable)
            .error(colorDrawable)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        Glide
            .with(context)
            .load(
                if (actor.imagePath == "") {
                    itemView.resources.getIdentifier(
                            "pic_no_actor_photo",
                            "drawable",
                            context.packageName
                    )
                } else {
                    actor.imagePath
                }
            )
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)

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