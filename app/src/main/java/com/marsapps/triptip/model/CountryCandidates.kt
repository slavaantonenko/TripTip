package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryCandidates {
    @SerializedName("candidates")
    @Expose
    val candidates: List<CountryImagesList> = arrayListOf()
    @SerializedName("status")
    @Expose
    val status: String = ""
}