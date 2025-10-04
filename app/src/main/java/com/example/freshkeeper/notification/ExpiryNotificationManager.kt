package com.example.freshkeeper.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.freshkeeper.grocery.model.GroceryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


@HiltWorker
class ExpiryNotificationWorker @AssistedInject constructor(
    private val repository: GroceryRepository,
    @Assisted private val appContext: Context, // Use this for getSystemService
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

            val today = System.currentTimeMillis()
            val groceries = repository.allGroceries.first()

            groceries.forEach { item ->
                item.expiryDate?.let { expiryDate ->
                    val daysLeft = ((expiryDate - today) / (1000 * 60 * 60 * 24)).toInt()
                    Log.d("ExpiryWorker", "${item.name} - Days left: $daysLeft")
                    if (daysLeft in 0..3) {
                        if (daysLeft == 0){
                            showNotification(
                                title = "Expiring soon!",
                                message = "${item.name} expiring today!"
                            )
                        }
                        else if (daysLeft == 1){
                            showNotification(
                                title = "Expiring soon!",
                                message = "${item.name}  expiring tomorrow!"
                            )
                        }
                        else {
                            showNotification(
                                title = "Expiring soon!",
                                message = "${item.name} is expiring in $daysLeft days!"
                            )
                        }

                    }
                }
            }
            return@withContext Result.success()


    }

    private fun showNotification(title: String, message: String) {
        val channelId = "expiry_channel"
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // Use appContext

        val channel = NotificationChannel(
            channelId,
            "Grocery Expiry Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(appContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_upload_you_tube) // Replace with your app's icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d("ExpiryWorker", "Notification shown for: $message")
    }
}
