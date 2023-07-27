package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidsList: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids_table ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate=:currentDate")
    fun getCurrentAsteroids(currentDate: String): LiveData<List<DatabaseAsteroid>>
}

