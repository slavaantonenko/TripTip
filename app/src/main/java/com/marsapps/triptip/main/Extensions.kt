package com.marsapps.triptip.main

import android.content.Context
import android.content.res.Resources
import android.preference.PreferenceManager
import android.util.TypedValue
import kotlin.math.roundToInt

/////////////////////////////////////// SharedPreferences /////////////////////////////////////////////////

// String is the pref name
fun String.saveBooleanToCache(context: Context?, value: Boolean) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.putBoolean(this, value)
    editor.apply()
}

// String is the pref name
fun String.getBooleanFromCache(context: Context?, defaultValue: Boolean): Boolean {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getBoolean(this, defaultValue)
}

/////////////////////////////////////// SharedPreferences /////////////////////////////////////////////////

/*
 * Convert pixels to dp.
 */
fun Int.pxToDP(resources: Resources): Int {
    return (this / resources.displayMetrics.density).roundToInt()
}

/*
 * Converting dp to pixels
 */
fun Int.dpToPx(resources: Resources): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics).roundToInt()
}