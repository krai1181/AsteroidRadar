package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.network.NasaImage
import kotlinx.coroutines.launch

enum class AsteroidFilter { TODAY, ALL }
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)

    private val repository = AsteroidRepository(database)
    
    val asteroidsList: LiveData<List<Asteroid>>
        get() =  filter.switchMap { 
            when (it) {
                AsteroidFilter.ALL -> return@switchMap repository.asteroids
                AsteroidFilter.TODAY -> return@switchMap repository.currentAsteroidsList
                else -> return@switchMap repository.asteroids
            }
        }

    private val _filter = MutableLiveData<AsteroidFilter>()
    val filter: LiveData<AsteroidFilter>
        get() = _filter

    private var _nasaImageObject = MutableLiveData<NasaImage>()
    val nasaImageObject: LiveData<NasaImage>
        get() = _nasaImageObject

    private val _navigateToChosenAsteroid = MutableLiveData<Asteroid?>()
    val navigateToChosenAsteroid: LiveData<Asteroid?>
        get() = _navigateToChosenAsteroid

    init {
        _filter.value = AsteroidFilter.ALL
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.updateNasaImage()
            _nasaImageObject.value = repository.nasaImage.value
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToChosenAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsCompleted() {
        _navigateToChosenAsteroid.value = null
    }
    
    fun setCurrentFilter(filter: AsteroidFilter) {
        _filter.value = filter
     
    }
    
}


