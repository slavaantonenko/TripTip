package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryImage {
    @SerializedName("height")
    @Expose
    val height: Int = 0
    @SerializedName("html_attributions")
    @Expose
    val htmlAttributions: List<String> = arrayListOf()
    @SerializedName("photo_reference")
    @Expose
    val photoReference: String = ""
    @SerializedName("width")
    @Expose
    val width: Int = 0
}