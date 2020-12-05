package com.example.academyproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ActorsAdapter(
        context: Context,
        var actors: List<Actor>
): RecyclerView.Adapter<ActorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(
                        if (viewType == FIRST)
                            R.layout.view_holder_actor_first
                        else
                            R.layout.view_holder_actor,
                        parent,
                        false
                )

        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) FIRST else NOT_FIRST

    override fun getItemCount(): Int = actors.size

    companion object {
        const val FIRST = 0
        const val NOT_FIRST = 1
    }
}