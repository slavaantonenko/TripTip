package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Country {
    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("topLevelDomain")
    @Expose
    val topLevelDomain: List<String> = arrayListOf()

    @SerializedName("alpha2Code")
    @Expose
    val alpha2Code: String = ""

    @SerializedName("alpha3Code")
    @Expose
    val alpha3Code: String = ""

    @SerializedName("callingCodes")
    @Expose
    val callingCodes: List<String> = arrayListOf()

    @SerializedName("capital")
    @Expose
    val capital: String = ""

    @SerializedName("altSpellings")
    @Expose
    val altSpellings: List<String> = arrayListOf()

    @SerializedName("region")
    @Expose
    val region: String = ""

    @SerializedName("subregion")
    @Expose
    val subregion: String = ""

    @SerializedName("population")
    @Expose
    val population: Int = 0

    @SerializedName("latlng")
    @Expose
    val latlng: List<Double> = arrayListOf()

    @SerializedName("demonym")
    @Expose
    val demonym: String = ""

    @SerializedName("area")
    @Expose
    val area: Double = 0.0

    @SerializedName("gini")
    @Expose
    val gini: Double = 0.0

    @SerializedName("timezones")
    @Expose
    val timezones: List<String> = arrayListOf()

    @SerializedName("borders")
    @Expose
    val borders: List<String> = arrayListOf()

    @SerializedName("nativeName")
    @Expose
    val nativeName: String = ""

    @SerializedName("numericCode")
    @Expose
    val numericCode: String = ""

    @SerializedName("currencies")
    @Expose
    val currencies: List<Currency> = arrayListOf()

    @SerializedName("languages")
    @Expose
    val languages: List<Language> = arrayListOf()

    @SerializedName("translations")
    @Expose
    val translations: Translations =
        Translations()

    @SerializedName("flag")
    @Expose
    val flag: String = ""

    @SerializedName("regionalBlocs")
    @Expose
    val regionalBlocs: List<RegionalBloc> = arrayListOf()

    @SerializedName("cioc")
    @Expose
    val cioc: String = ""
}