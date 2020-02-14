package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegionalBloc {
    @SerializedName("acronym")
    @Expose
    val acronym: String = ""
    @SerializedName("name")
    @Expose
    val name: String = ""
    @SerializedName("otherAcronyms")
    @Expose
    val otherAcronyms: List<Any> = arrayListOf()
    @SerializedName("otherNames")
    @Expose
    val otherNames: List<Any> = arrayListOf()
}