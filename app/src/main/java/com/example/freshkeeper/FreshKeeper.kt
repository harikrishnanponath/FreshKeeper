package com.example.freshkeeper

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.freshkeeper.notification.ExpiryNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class FreshKeeper : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private var workerFactoryInstanceCode: Int = 0

    override fun onCreate() {

        super.onCreate()
        workerFactoryInstanceCode = System.identityHashCode(workerFactory)

//        val workRequest = OneTimeWorkRequestBuilder<ExpiryNotificationWorker>().build()
//        WorkManager.getInstance(this).enqueue(workRequest) // This call will trigger getWorkManagerConfiguration if WM isn't initialized

        scheduleDailyWorker()
    }

    override val workManagerConfiguration: Configuration
        get() {
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .setMinimumLoggingLevel(Log.VERBOSE) // Use VERBOSE for max WorkManager logs
                .build()
        }

    private fun scheduleDailyWorker() {
        val workRequest = PeriodicWorkRequestBuilder<ExpiryNotificationWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(1, TimeUnit.HOURS) // optional: first run after 1h
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ExpiryNotificationWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}

