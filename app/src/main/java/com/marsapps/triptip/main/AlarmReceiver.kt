package com.marsapps.triptip.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.marsapps.triptip.R


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context, intent: Intent?) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val notification: Notification = intent!!.getParcelableExtra(NOTIFICATION)
//        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
//        notificationManager.notify(id, notification)
//        val prefs = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context))
//        val sharedPrefEditor = prefs.edit()
//
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Daily Notification", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Daily Notification"
            notificationManager.createNotificationChannel(channel)
//        }

        val builder = NotificationCompat.Builder(context, "default")
        builder.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("{Time to watch some cool stuff!}")
            .setContentTitle("My Cool App")
            .setContentText("Time to watch some cool stuff!")
            .setContentInfo("INFO")
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, builder.build())
//
//        val nextNotifyTime: Calendar = Calendar.getInstance()
//        nextNotifyTime.add(Calendar.DATE, 1)
//        sharedPrefEditor.putLong("nextNotifyTime", nextNotifyTime.timeInMillis)
//        sharedPrefEditor.apply()
    }
}