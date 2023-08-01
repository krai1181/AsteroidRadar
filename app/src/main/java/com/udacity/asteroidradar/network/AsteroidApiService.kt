package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Constants.NASA_IMAGE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val API_KEY = BuildConfig.API_KEY

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

private val retrofit2 = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(NASA_IMAGE_URL)
    .build()


interface AsteroidApiService {
    @GET("neo/rest/v1/feed?api_key=$API_KEY")
    fun getAsteroidData(): Call<String>
}

interface NasaImageApiService{
    @GET("apod?api_key=$API_KEY")
    suspend fun getNasaImageObject(): NasaImage
}

object AsteroidApi{
    val retrofitService: AsteroidApiService by lazy{
        retrofit.create(AsteroidApiService::class.java)
    }
    
    val nasaImageRetrofitService: NasaImageApiService by lazy { 
        retrofit2.create(NasaImageApiService::class.java)
    }
}
