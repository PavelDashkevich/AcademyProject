package com.example.academyproject.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.*
import androidx.core.net.toUri
import com.example.academyproject.R
import com.example.academyproject.models.Movie

class Notifications(private val context: Context) {
    companion object {
        private const val CHANNEL_NEW_MOVIES_TOP_RATED = "new_movies_top_rated"
        private const val REQUEST_CONTENT = 1
        private const val TAG_MOVIE = "movie"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    private fun initializeChannel() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MOVIES_TOP_RATED) == null) {
            val channel = NotificationChannelCompat
                .Builder(CHANNEL_NEW_MOVIES_TOP_RATED, NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName(context.getString(R.string.channel_new_movie))
                .setDescription(context.getString(R.string.channel_new_movie_description))
                .build()

            notificationManagerCompat.createNotificationChannel(channel)
        }
    }

    fun showNotification(movie: Movie) {
        val link = "https://academyproject.example.com/movie/${movie.id}".toUri()
        val movieIntent = Intent(Intent.ACTION_VIEW)
        movieIntent.data = link

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CONTENT,
            movieIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        initializeChannel()

        val notification = NotificationCompat
            .Builder(context, CHANNEL_NEW_MOVIES_TOP_RATED)
            .setContentTitle(context.getString(R.string.notification_top_rated_new_movie))
            .setContentText(movie.title)
            .setSmallIcon(R.drawable.ic_star_highlighted)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManagerCompat.notify(TAG_MOVIE, movie.id, notification)
    }
}