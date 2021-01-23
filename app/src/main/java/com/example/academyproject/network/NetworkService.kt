package com.example.academyproject.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

private const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_IMG_SIZE = "w500"
const val BACKDROP_IMG_SIZE = "w1280"
const val PROFILE_IMG_SIZE = "w185"

private val json = Json { ignoreUnknownKeys = true }

private val contentType = "application/json".toMediaType()

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(ApiKeyInterceptor())
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(httpClient)
    .addConverterFactory(json.asConverterFactory(contentType))
    .build()

val theMovieDbApiService: TheMovieDbApiService = retrofit.create()

var imageBaseUrl: String = ""