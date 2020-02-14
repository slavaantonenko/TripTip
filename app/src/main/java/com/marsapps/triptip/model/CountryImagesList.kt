package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryImagesList {
    @SerializedName("photos")
    @Expose
    val photos: List<CountryImage>? = arrayListOf()
//    @SerializedName("totalHits")
//    @Expose
//    val totalHits: Int = 0
//
//    @SerializedName("hits")
//    @Expose
//    val hits: List<CountryImage> = arrayListOf()
//
//    @SerializedName("total")
//    @Expose
//    val total: Int = 0
}