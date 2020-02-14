package com.marsapps.triptip.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.marsapps.triptip.R
import com.marsapps.triptip.main.Constants.Companion.Data.COUNTRY_NAME_DATA
import com.marsapps.triptip.main.Constants.Companion.Data.NOTIFICATION_ID_DATA
import com.marsapps.triptip.main.Constants.Companion.Extras.COUNTRY_NOTIFICATION_EXTRA
import com.marsapps.triptip.main.Constants.Companion.Notifications.ID
import com.marsapps.triptip.main.Constants.Companion.Notifications.NAME

class MonthTipNotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context.applicationContext, workerParams) {
    override fun doWork(): Result {
        return try {
            notify(applicationContext, inputData.getString(COUNTRY_NAME_DATA), inputData.getInt(NOTIFICATION_ID_DATA, 0))
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun notify(appContext: Context, countryName: String?, notificationID: Int) {
        val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(appContext, MainActivity::class.java)
        notificationIntent.putExtra(COUNTRY_NOTIFICATION_EXTRA, countryName)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(appContext, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = NAME
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(appContext, ID)
        builder.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.icon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(appContext.getString(R.string.notification_message, countryName)))
            .setContentTitle(appContext.getString(R.string.app_name))
            .setContentText(countryName)
            .setContentInfo(appContext.getString(R.string.tip))
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationID, builder.build())
    }
}