package com.marsapps.triptip.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import java.util.*


class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Objects.equals(intent.action, "android.intent.action.BOOT_COMPLETED")) { // on device boot complete, reset the alarm
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context))
            val calendar: Calendar = Calendar.getInstance()
            val newCalendar: Calendar = GregorianCalendar()

            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, 19)
            calendar.set(Calendar.MINUTE, 7)
            calendar.set(Calendar.SECOND, 1)
            newCalendar.timeInMillis = sharedPref.getLong("nextNotifyTime", Calendar.getInstance().timeInMillis)

            if (calendar.after(newCalendar))
                calendar.add(Calendar.HOUR, 1)

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }
}