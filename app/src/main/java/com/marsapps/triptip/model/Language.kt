package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Language {
    @SerializedName("iso639_1")
    @Expose
    val iso6391: String = ""

    @SerializedName("iso639_2")
    @Expose
    val iso6392: String = ""

    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("nativeName")
    @Expose
    val nativeName: String = ""
}