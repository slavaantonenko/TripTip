package com.marsapps.triptip.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "countries_table")
data class CountryModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var nativeName: String = "",
    var code: String = "",
    var secondaryCode: String = "",
    var callingCode: String = "",
    var capital: String = "",
    var region: String = "",
    var population: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var area: Double = 0.0,
    var timezone: String = "",
    var currency: String = "",
    var language: String = "",
    var nativeLanguage: String = "",
    var flag: String = "",
    var isInTravelList: Boolean = false,
    var image: String = "",
    var beenThere: Boolean = false
) : Parcelable
