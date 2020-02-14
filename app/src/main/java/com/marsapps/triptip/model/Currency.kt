package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Currency {
    @SerializedName("code")
    @Expose
    val code: String = ""

    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("symbol")
    @Expose
    val symbol: String = ""
}