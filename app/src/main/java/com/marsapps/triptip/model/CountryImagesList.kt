package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryImagesList {
    @SerializedName("photos")
    @Expose
    val photos: List<CountryImage>? = arrayListOf()
}