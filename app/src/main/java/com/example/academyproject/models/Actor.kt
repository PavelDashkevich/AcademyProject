package com.example.academyproject.models

import com.example.academyproject.network.PROFILE_IMG_SIZE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    var imagePath: String?
) {
    fun applyImageBaseUrl(imageBaseUrl: String) {
        imagePath = if (imagePath != null) "$imageBaseUrl$PROFILE_IMG_SIZE/$imagePath" else ""
    }
}