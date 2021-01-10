package com.example.academyproject.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academyproject.R
import com.example.academyproject.models.data.Actor

class ActorsAdapter(
    private var actors: List<Actor>
): RecyclerView.Adapter<ActorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.actor_item, parent, false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int = actors.size
}