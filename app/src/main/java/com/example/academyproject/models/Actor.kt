package com.example.academyproject.models

import com.example.academyproject.network.PROFILE_IMG_SIZE

data class Actor(
    val id: Int,
    val name: String,
    var picture: String?
) {
    fun applyBaseUrl(baseUrl: String) {
        if (picture != null)
            picture = "$baseUrl$PROFILE_IMG_SIZE/$picture"
    }
}