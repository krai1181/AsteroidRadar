package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.getCurrentFormattedDate
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.NasaImage
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class NASAImageApiStatus { DONE, ERROR }

class AsteroidRepository(private val database: AsteroidDatabase) {

    var asteroids = database.asteroidDao.getAsteroids().map {
        it.asDomainModel()
    }
    
    var currentAsteroidsList = database.asteroidDao.getCurrentAsteroids(getCurrentFormattedDate()).map {
        it.asDomainModel()
    } 
    
    var weeklyAsteroidsList = database.asteroidDao.getWeeklyAsteroids(getCurrentFormattedDate(), 
        getNextSevenDaysFormattedDates().last()).map { 
        it.asDomainModel()
    }

    private val _nasaImage = MutableLiveData<NasaImage>()
    val nasaImage: LiveData<NasaImage>
        get() = _nasaImage

    private val _status = MutableLiveData<NASAImageApiStatus>()
    val status: LiveData<NASAImageApiStatus>
        get() = _status

    private val _response = MutableLiveData<String>()


    fun refreshAsteroids() {
        AsteroidApi.retrofitService.getAsteroidData().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
                insertData(response.body())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure ${t.message}"
            }

        })

    }

    private fun insertData(data: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                data?.let {
                    val jsonObject = JSONObject(it)
                    val asteroidProperties = parseAsteroidsJsonResult(jsonObject)
                    val networkAsteroidContainer = NetworkAsteroidContainer(asteroidProperties)
                    database.asteroidDao.insertAll(*networkAsteroidContainer.asDatabaseModel())
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
            }

        }
    }

    suspend fun updateNasaImage() {
        try {
            _status.value = NASAImageApiStatus.DONE
            val result = AsteroidApi.nasaImageRetrofitService.getNasaImageObject()
            result.let {
                _nasaImage.value = it
            }
        } catch (e: Exception) {
            _status.value = NASAImageApiStatus.ERROR
        }
    }
}
