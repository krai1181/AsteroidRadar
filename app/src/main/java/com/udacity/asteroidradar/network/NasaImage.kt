package com.udacity.asteroidradar.network

import com.squareup.moshi.Json

data class NasaImage(
    val date: String,
    val explanation: String,
    val hdurl: String,
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "service_version")
    val serviceVersion: String,
    val title: String,
    val url: String
)