package com.udacity.asteroidradar.network

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.parcelize.Parcelize


@Parcelize
data class Asteroid(
val id: Long, 
val codename: String,
val closeApproachDate: String,
val absoluteMagnitude: Double, 
val estimatedDiameter: Double,
val relativeVelocity: Double, 
val distanceFromEarth: Double,
val isPotentiallyHazardous: Boolean) : Parcelable


@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<Asteroid>)

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map { 
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
        )
    }.toTypedArray()
}

