package com.udacity.asteroidradar

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.work.CacheDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DevApplication: Application() {
    
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
    
    private fun delayedInit(){
        applicationScope.launch { 
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply { 
                setRequiresDeviceIdle(true)
            }.build()
        
        val repeatingRequest = PeriodicWorkRequestBuilder<CacheDataWorker>(
            1,
            TimeUnit.DAYS,
        ).setConstraints(constrains)
            .build()
        
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            CacheDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}